package org.dimas.medextest.feature.more.loadlab.presentation.mvi

import androidx.compose.runtime.Stable
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.media.PickedFile
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScenario

@Stable
interface LoadLabActions {
    fun onModeSelect(mode: ExecutionMode)

    fun onScaleSelect(scale: LoadScale)

    fun onScenarioClick(scenario: LoadScenario)

    fun onFilePicked(file: PickedFile?)

    fun onPickerError(error: Throwable)

    fun onCancelClick(taskId: Int)

    fun onCancelAllClick()

    fun onClearFinishedClick()

    fun onReportCopied()
}
