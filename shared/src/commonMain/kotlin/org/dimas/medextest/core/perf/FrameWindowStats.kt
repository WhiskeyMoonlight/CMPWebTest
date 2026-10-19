package org.dimas.medextest.core.perf

import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.DurationUnit

data class FrameWindowStats(
    val frames: Int,
    val slowFrames: Int,
    val frozenFrames: Int,
    val worstFrame: Duration,
    val worstLag: Duration,
    val duration: Duration,
) {
    val averageFps: Int
        get() {
            val seconds = duration.toDouble(DurationUnit.SECONDS)
            return if (seconds <= 0.0) 0 else (frames / seconds).roundToInt()
        }

    val isMeasured: Boolean get() = frames > 0
}
