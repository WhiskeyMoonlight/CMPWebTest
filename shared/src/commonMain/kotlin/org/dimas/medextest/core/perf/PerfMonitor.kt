package org.dimas.medextest.core.perf

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.TimeSource

class PerfMonitor(private val timeSource: TimeSource = TimeSource.Monotonic) {
    private val windows = MutableStateFlow<List<PerfWindow>>(emptyList())
    private val snapshotState = MutableStateFlow(PerfSnapshot())

    private var lastFrameNanos = NO_FRAME
    private var periodStartNanos = NO_FRAME
    private var periodFrames = 0
    private var periodWorstFrameNanos = 0L
    private var periodWorstLag = Duration.ZERO

    val snapshot: StateFlow<PerfSnapshot> = snapshotState.asStateFlow()

    val isMeasuring: Flow<Boolean> = windows.map { it.isNotEmpty() }.distinctUntilChanged()

    fun openWindow(): PerfWindow {
        val window = PerfWindow(timeSource)
        windows.update { it + window }
        return window
    }

    fun closeWindow(window: PerfWindow): FrameWindowStats {
        windows.update { it - window }
        return window.stats()
    }

    fun resetFrameClock() {
        lastFrameNanos = NO_FRAME
        periodStartNanos = NO_FRAME
        periodFrames = 0
        periodWorstFrameNanos = 0L
        periodWorstLag = Duration.ZERO
    }

    fun onFrame(frameTimeNanos: Long) {
        val previous = lastFrameNanos
        lastFrameNanos = frameTimeNanos
        if (previous == NO_FRAME) {
            periodStartNanos = frameTimeNanos
            return
        }
        val interval = frameTimeNanos - previous
        windows.value.forEach { it.recordFrame(interval.nanoseconds) }
        periodFrames++
        periodWorstFrameNanos = maxOf(periodWorstFrameNanos, interval)
        val periodLength = frameTimeNanos - periodStartNanos
        if (periodLength >= SNAPSHOT_PERIOD_NANOS) publish(periodLength)
    }

    fun onLag(lag: Duration) {
        val clamped = lag.coerceAtLeast(Duration.ZERO)
        windows.value.forEach { it.recordLag(clamped) }
        periodWorstLag = maxOf(periodWorstLag, clamped)
    }

    private fun publish(periodLength: Long) {
        snapshotState.value = PerfSnapshot(
            fps = (periodFrames * NANOS_IN_SECOND / periodLength).roundToInt(),
            worstFrameMs = (periodWorstFrameNanos / NANOS_IN_MILLI).toInt(),
            lagMs = periodWorstLag.inWholeMilliseconds.toInt(),
        )
        periodStartNanos = lastFrameNanos
        periodFrames = 0
        periodWorstFrameNanos = 0L
        periodWorstLag = Duration.ZERO
    }

    private companion object {
        const val NO_FRAME = -1L
        const val NANOS_IN_MILLI = 1_000_000L
        const val NANOS_IN_SECOND = 1_000_000_000.0
        const val SNAPSHOT_PERIOD_NANOS = 250_000_000L
    }
}
