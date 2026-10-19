package org.dimas.medextest.core.perf

import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

@OptIn(ExperimentalAtomicApi::class)
class PerfWindow internal constructor(timeSource: TimeSource) {
    private val started = timeSource.markNow()
    private val frames = AtomicInt(0)
    private val slowFrames = AtomicInt(0)
    private val frozenFrames = AtomicInt(0)
    private val worstFrameMicros = AtomicInt(0)
    private val worstLagMicros = AtomicInt(0)

    internal fun recordFrame(interval: Duration) {
        frames.addAndFetch(1)
        if (interval > SLOW_FRAME) slowFrames.addAndFetch(1)
        if (interval > FROZEN_FRAME) frozenFrames.addAndFetch(1)
        worstFrameMicros.updateMax(interval.inWholeMicroseconds.toIntSaturated())
    }

    internal fun recordLag(lag: Duration) {
        worstLagMicros.updateMax(lag.inWholeMicroseconds.toIntSaturated())
    }

    internal fun stats(): FrameWindowStats = FrameWindowStats(
        frames = frames.load(),
        slowFrames = slowFrames.load(),
        frozenFrames = frozenFrames.load(),
        worstFrame = worstFrameMicros.load().microseconds,
        worstLag = worstLagMicros.load().microseconds,
        duration = started.elapsedNow(),
    )

    private fun AtomicInt.updateMax(candidate: Int) {
        while (true) {
            val current = load()
            if (candidate <= current || compareAndSet(current, candidate)) return
        }
    }

    private fun Long.toIntSaturated(): Int = coerceIn(0L, Int.MAX_VALUE.toLong()).toInt()

    companion object {
        val SLOW_FRAME: Duration = 17.milliseconds
        val FROZEN_FRAME: Duration = 700.milliseconds
    }
}
