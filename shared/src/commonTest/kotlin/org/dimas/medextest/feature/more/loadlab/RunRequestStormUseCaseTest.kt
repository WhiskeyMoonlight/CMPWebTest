package org.dimas.medextest.feature.more.loadlab

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.dimas.medextest.core.background.TaskProgress
import org.dimas.medextest.core.compute.ComputeExecutor
import org.dimas.medextest.core.compute.CoroutineYielder
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.compute.NoWorkerBackend
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunRequestStormUseCase
import org.dimas.medextest.testutil.FakeSyncRepository
import org.dimas.medextest.testutil.testExecutor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

class RunRequestStormUseCaseTest {
    private fun expectedRevisions(total: Int, skipped: Set<Int> = emptySet()): Long =
        (0 until total).filter { it !in skipped }.sumOf { (it % FakeSyncRepository.REVISIONS + 1).toLong() }

    @Test
    fun every_request_is_sent_and_concurrency_stays_bounded() = runTest {
        val repository = FakeSyncRepository()
        val useCase = RunRequestStormUseCase(repository, testExecutor(), requests = 500, concurrency = 16)
        val progress = TaskProgress()

        val summary = useCase(ExecutionMode.Default, LoadScale.X1, progress)

        assertEquals(
            "500 запросов · одновременно до 16 · повторов 0 · ошибок 0 · ревизий ${expectedRevisions(500)}",
            summary,
        )
        assertEquals(1f, progress.fraction)
    }

    @Test
    fun transient_failures_are_retried_and_permanent_ones_counted() = runTest {
        val repository = FakeSyncRepository(failingOnce = setOf(1, 2, 3), alwaysFailing = setOf(7))
        val useCase = RunRequestStormUseCase(repository, testExecutor(), requests = 10, concurrency = 4)

        val summary = useCase(ExecutionMode.Cooperative, LoadScale.X1, TaskProgress())

        assertEquals(
            "10 запросов · одновременно до 4 · повторов 5 · ошибок 1 · ревизий ${expectedRevisions(10, skipped = setOf(7))}",
            summary,
        )
        assertEquals(15, repository.callCount)
    }

    @Test
    fun counters_stay_exact_on_real_threads() = runTest {
        val repository = FakeSyncRepository(latency = 1.milliseconds)
        val executor = ComputeExecutor(
            mainDispatcher = Dispatchers.Default,
            defaultDispatcher = Dispatchers.Default,
            uiYielder = CoroutineYielder(),
            workers = NoWorkerBackend,
            cpuCores = 8,
        )
        val useCase = RunRequestStormUseCase(repository, executor, requests = 2_000, concurrency = 64)
        val progress = TaskProgress()

        val summary = withContext(Dispatchers.Default) { useCase(ExecutionMode.Parallel, LoadScale.X1, progress) }

        assertEquals(2_000, repository.callCount)
        assertEquals(2_000, progress.completedSteps)
        assertEquals("ревизий ${expectedRevisions(2_000)}", summary.substringAfterLast(" · "))
    }
}
