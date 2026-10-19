package org.dimas.medextest.core.background

sealed interface TaskStatus {
    data object Running : TaskStatus

    data class Completed(val summary: String) : TaskStatus

    data class Failed(val reason: String) : TaskStatus

    data object Cancelled : TaskStatus
}
