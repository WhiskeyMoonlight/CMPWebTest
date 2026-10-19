package org.dimas.medextest.feature.more.loadlab.presentation.ui

import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScenario

internal object LoadLabTags {
    const val PICK_FILE = "loadlab.pickFile"
    const val CANCEL_ALL = "loadlab.cancelAll"
    const val CLEAR = "loadlab.clear"
    const val COPY_REPORT = "loadlab.copyReport"

    fun scenario(scenario: LoadScenario): String = "loadlab.scenario.${scenario.name}"

    fun mode(mode: ExecutionMode): String = "loadlab.mode.${mode.name}"

    fun task(id: Int): String = "loadlab.task.$id"

    fun cancel(id: Int): String = "loadlab.cancel.$id"
}
