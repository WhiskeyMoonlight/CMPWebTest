package org.dimas.medextest.core.perf

data class PerfSnapshot(
    val fps: Int = 0,
    val worstFrameMs: Int = 0,
    val lagMs: Int = 0,
)
