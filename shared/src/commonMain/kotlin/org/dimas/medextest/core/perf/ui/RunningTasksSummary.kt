package org.dimas.medextest.core.perf.ui

import org.dimas.medextest.core.background.BackgroundTask
import org.dimas.medextest.core.background.TaskStatus

data class RunningTasksSummary(val count: Int, val progress: Float) {
    companion object {
        fun of(tasks: List<BackgroundTask>): RunningTasksSummary? {
            val running = tasks.filter { it.status == TaskStatus.Running }
            if (running.isEmpty()) return null
            return RunningTasksSummary(count = running.size, progress = running.map { it.progress }.average().toFloat())
        }
    }
}
