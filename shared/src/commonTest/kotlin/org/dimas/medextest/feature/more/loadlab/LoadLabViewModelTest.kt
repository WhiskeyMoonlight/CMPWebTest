package org.dimas.medextest.feature.more.loadlab

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.dimas.medextest.core.background.BackgroundTasks
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.compute.WorkerBackend
import org.dimas.medextest.core.compute.NoWorkerBackend
import org.dimas.medextest.core.media.PickedFile
import org.dimas.medextest.core.perf.PerfMonitor
import org.dimas.medextest.core.platform.PlatformInfo
import org.dimas.medextest.core.platform.PlatformKind
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScenario
import org.dimas.medextest.feature.more.loadlab.domain.usecase.HashPickedFileUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunAttachmentHashUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunLabImportUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunRequestStormUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunScanFilterUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunScenarioUseCase
import org.dimas.medextest.feature.more.loadlab.presentation.LoadLabViewModel
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUiStatus
import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabSideEffect
import org.dimas.medextest.testutil.FakeLabRepository
import org.dimas.medextest.testutil.FakeSyncRepository
import org.dimas.medextest.testutil.LocalWorkerBackend
import org.dimas.medextest.testutil.MainDispatcherTest
import org.dimas.medextest.testutil.awaitStateMatching
import org.dimas.medextest.testutil.testExecutor
import org.orbitmvi.orbit.test.testWithInternalState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoadLabViewModelTest : MainDispatcherTest() {
    private fun TestScope.backgroundTasks() = BackgroundTasks(
        scope = CoroutineScope(SupervisorJob() + StandardTestDispatcher(testScheduler)),
        perfMonitor = PerfMonitor(testScheduler.timeSource),
        timeSource = testScheduler.timeSource,
    )

    private fun TestScope.viewModel(tasks: BackgroundTasks, workers: WorkerBackend = NoWorkerBackend): LoadLabViewModel {
        val executor = testExecutor(workers = workers)
        val scenarios = RunScenarioUseCase(
            labImport = RunLabImportUseCase(FakeLabRepository(), executor, pages = 2),
            attachmentHash = RunAttachmentHashUseCase(executor, files = 2, fileSize = 4_096, timeSource = testScheduler.timeSource),
            scanFilter = RunScanFilterUseCase(executor, width = 40, height = 30, radius = 2),
            requestStorm = RunRequestStormUseCase(FakeSyncRepository(), executor, requests = 20, concurrency = 4),
        )
        return LoadLabViewModel(
            backgroundTasks = tasks,
            runScenario = scenarios,
            hashPickedFile = HashPickedFileUseCase(executor, testScheduler.timeSource),
            executor = executor,
            platformInfo = PlatformInfo(name = "Test", kind = PlatformKind.Web, cpuCores = 6),
        )
    }

    @Test
    fun initial_state_describes_platform_and_hides_worker_without_pool() = runTest {
        val state = viewModel(backgroundTasks()).container.stateFlow.value

        assertEquals(6, state.cpuCores)
        assertEquals(0, state.workerPoolSize)
        assertFalse(ExecutionMode.Worker in state.availableModes)
        assertEquals(ExecutionMode.Default, state.mode)
    }

    @Test
    fun worker_mode_is_offered_when_pool_exists() = runTest {
        val state = viewModel(backgroundTasks(), workers = LocalWorkerBackend(poolSize = 5)).container.stateFlow.value

        assertTrue(ExecutionMode.Worker in state.availableModes)
        assertEquals(5, state.workerPoolSize)
    }

    @Test
    fun every_scenario_runs_to_completion_in_selected_mode() = runTest {
        val tasks = backgroundTasks()

        viewModel(tasks).testWithInternalState(this) {
            runOnCreate()
            containerHost.onModeSelect(ExecutionMode.Cooperative)
            containerHost.onScaleSelect(LoadScale.X4)
            LoadScenario.entries.forEach(containerHost::onScenarioClick)
            advanceUntilIdle()

            val settled = awaitStateMatching { state ->
                state.tasks.size == LoadScenario.entries.size && state.tasks.all { it.status == TaskUiStatus.Completed }
            }
            assertEquals(LoadScenario.entries.map { it.title }.reversed(), settled.tasks.map { it.title })
            assertTrue(settled.tasks.all { it.details == "Нарезка · ×4" }, settled.tasks.toString())
            assertTrue(settled.tasks.all { it.result != null && it.perf != null })
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun cancel_marks_task_cancelled() = runTest {
        val tasks = backgroundTasks()

        viewModel(tasks).testWithInternalState(this) {
            runOnCreate()
            containerHost.onScenarioClick(LoadScenario.RequestStorm)
            awaitStateMatching { it.tasks.singleOrNull()?.status == TaskUiStatus.Running }

            containerHost.onCancelClick(tasks.tasks.value.single().id)
            advanceUntilIdle()

            awaitStateMatching { it.tasks.single().status == TaskUiStatus.Cancelled }
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun picked_file_is_hashed_and_missing_file_ignored() = runTest {
        val tasks = backgroundTasks()
        val bytes = ByteArray(1_000) { it.toByte() }

        viewModel(tasks).testWithInternalState(this) {
            runOnCreate()
            containerHost.onFilePicked(null)
            containerHost.onFilePicked(PickedFile("scan.png", "image/png", bytes.size.toLong(), reader = { bytes }))
            advanceUntilIdle()

            val settled = awaitStateMatching { it.tasks.singleOrNull()?.status == TaskUiStatus.Completed }
            assertEquals("SHA-256 файла", settled.tasks.single().title)
            assertTrue(settled.tasks.single().result.orEmpty().startsWith("scan.png"))
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun clear_finished_removes_completed_tasks() = runTest {
        val tasks = backgroundTasks()

        viewModel(tasks).testWithInternalState(this) {
            runOnCreate()
            containerHost.onScenarioClick(LoadScenario.AttachmentHash)
            advanceUntilIdle()
            awaitStateMatching { it.hasFinishedTasks }

            containerHost.onClearFinishedClick()
            awaitStateMatching { it.tasks.isEmpty() }
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun picker_error_and_copied_report_show_messages() = runTest {
        viewModel(backgroundTasks()).testWithInternalState(this) {
            containerHost.onPickerError(IllegalStateException("Нет доступа к файлам"))
            expectSideEffect(LoadLabSideEffect.ShowMessage("Нет доступа к файлам"))

            containerHost.onReportCopied()
            expectSideEffect(LoadLabSideEffect.ShowMessage("Отчёт скопирован в буфер обмена"))
        }
    }
}
