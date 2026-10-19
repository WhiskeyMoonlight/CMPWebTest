package org.dimas.medextest.feature.more.loadlab.presentation

import androidx.lifecycle.ViewModel
import org.dimas.medextest.core.background.BackgroundTask
import org.dimas.medextest.core.background.BackgroundTasks
import org.dimas.medextest.core.compute.ComputeExecutor
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.media.PickedFile
import org.dimas.medextest.core.platform.PlatformInfo
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScenario
import org.dimas.medextest.feature.more.loadlab.domain.usecase.HashPickedFileUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunScenarioUseCase
import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabActions
import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabSideEffect
import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabState
import org.dimas.medextest.feature.more.loadlab.presentation.util.label
import org.dimas.medextest.feature.more.loadlab.presentation.util.toUi
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class LoadLabViewModel(
    private val backgroundTasks: BackgroundTasks,
    private val runScenario: RunScenarioUseCase,
    private val hashPickedFile: HashPickedFileUseCase,
    executor: ComputeExecutor,
    platformInfo: PlatformInfo,
) : ViewModel(), OrbitContainerHost<LoadLabState, LoadLabState, LoadLabSideEffect>, LoadLabActions {

    override val container: OrbitContainer<LoadLabState, LoadLabState, LoadLabSideEffect> = orbitContainer(
        initialState = LoadLabState(
            platformName = platformInfo.name,
            cpuCores = platformInfo.cpuCores,
            workerPoolSize = executor.workerPoolSize,
            availableModes = executor.availableModes,
        ),
    ) {
        repeatOnSubscription {
            backgroundTasks.tasks.collect { tasks -> reduce { state.copy(tasks = tasks.map(BackgroundTask::toUi)) } }
        }
    }

    override fun onModeSelect(mode: ExecutionMode) {
        intent { reduce { state.copy(mode = mode) } }
    }

    override fun onScaleSelect(scale: LoadScale) {
        intent { reduce { state.copy(scale = scale) } }
    }

    override fun onScenarioClick(scenario: LoadScenario) {
        intent {
            val mode = state.mode
            val scale = state.scale
            backgroundTasks.launch(title = scenario.title, details = "${mode.label} · ${scale.label}") { progress ->
                runScenario(scenario, mode, scale, progress)
            }
        }
    }

    override fun onFilePicked(file: PickedFile?) {
        if (file == null) return
        intent {
            val mode = state.mode
            backgroundTasks.launch(title = "SHA-256 файла", details = mode.label) { progress -> hashPickedFile(file, mode, progress) }
        }
    }

    override fun onPickerError(error: Throwable) {
        intent { postSideEffect(LoadLabSideEffect.ShowMessage(error.message ?: "Не удалось открыть выбор файла")) }
    }

    override fun onCancelClick(taskId: Int) = backgroundTasks.cancel(taskId)

    override fun onCancelAllClick() = backgroundTasks.cancelAll()

    override fun onClearFinishedClick() = backgroundTasks.clearFinished()

    override fun onReportCopied() {
        intent { postSideEffect(LoadLabSideEffect.ShowMessage("Отчёт скопирован в буфер обмена")) }
    }
}
