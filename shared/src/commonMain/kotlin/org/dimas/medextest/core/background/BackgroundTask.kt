package org.dimas.medextest.core.background

import org.dimas.medextest.core.perf.FrameWindowStats
import kotlin.time.Duration

data class BackgroundTask(
    val id: Int,
    val title: String,
    val details: String,
    val status: TaskStatus = TaskStatus.Running,
    val progress: Float = 0f,
    val elapsed: Duration = Duration.ZERO,
    val perf: FrameWindowStats? = null,
)
