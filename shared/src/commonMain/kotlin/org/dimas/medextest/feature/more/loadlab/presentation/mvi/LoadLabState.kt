package org.dimas.medextest.feature.more.loadlab.presentation.mvi

import androidx.compose.runtime.Immutable
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUi
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUiStatus

@Immutable
data class LoadLabState(
    val platformName: String,
    val cpuCores: Int,
    val workerPoolSize: Int,
    val availableModes: List<ExecutionMode>,
    val mode: ExecutionMode = ExecutionMode.Default,
    val scale: LoadScale = LoadScale.X1,
    val tasks: List<TaskUi> = emptyList(),
) {
    val hasRunningTasks: Boolean get() = tasks.any { it.status == TaskUiStatus.Running }

    val hasFinishedTasks: Boolean get() = tasks.any { it.status != TaskUiStatus.Running }
}
