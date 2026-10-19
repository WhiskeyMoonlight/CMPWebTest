package org.dimas.medextest.ui

import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.media.PickedFile
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScenario
import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabActions

class FakeLoadLabActions : LoadLabActions {
    val calls = mutableListOf<String>()

    override fun onModeSelect(mode: ExecutionMode) {
        calls += "mode:${mode.name}"
    }

    override fun onScaleSelect(scale: LoadScale) {
        calls += "scale:${scale.name}"
    }

    override fun onScenarioClick(scenario: LoadScenario) {
        calls += "scenario:${scenario.name}"
    }

    override fun onFilePicked(file: PickedFile?) {
        calls += "file:${file?.name}"
    }

    override fun onPickerError(error: Throwable) {
        calls += "pickerError"
    }

    override fun onCancelClick(taskId: Int) {
        calls += "cancel:$taskId"
    }

    override fun onCancelAllClick() {
        calls += "cancelAll"
    }

    override fun onClearFinishedClick() {
        calls += "clear"
    }

    override fun onReportCopied() {
        calls += "copied"
    }
}
