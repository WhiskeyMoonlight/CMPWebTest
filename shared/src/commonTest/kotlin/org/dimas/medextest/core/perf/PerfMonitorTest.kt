package org.dimas.medextest.core.perf

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource

class PerfMonitorTest {
    private val time = TestTimeSource()
    private val monitor = PerfMonitor(time)

    private fun frames(vararg millis: Long) = millis.forEach { monitor.onFrame(it * NANOS_IN_MILLI) }

    @Test
    fun window_counts_frames_and_classifies_jank() {
        val window = monitor.openWindow()

        frames(0, 16, 56, 856)

        val stats = monitor.closeWindow(window)
        assertEquals(3, stats.frames)
        assertEquals(2, stats.slowFrames)
        assertEquals(1, stats.frozenFrames)
        assertEquals(800.milliseconds, stats.worstFrame)
    }

    @Test
    fun frames_before_window_opened_are_ignored() {
        frames(0, 500, 516)
        val window = monitor.openWindow()

        frames(532)

        assertEquals(1, monitor.closeWindow(window).frames)
    }

    @Test
    fun lag_is_tracked_and_negative_samples_are_clamped() {
        val window = monitor.openWindow()

        monitor.onLag((-5).milliseconds)
        monitor.onLag(120.milliseconds)
        monitor.onLag(30.milliseconds)

        assertEquals(120.milliseconds, monitor.closeWindow(window).worstLag)
    }

    @Test
    fun average_fps_uses_window_duration() {
        val window = monitor.openWindow()
        frames(*LongArray(61) { it * 1000L / 60 })
        time += 1.seconds

        val stats = monitor.closeWindow(window)

        assertEquals(60, stats.averageFps)
        assertTrue(stats.isMeasured)
    }

    @Test
    fun window_without_frames_is_not_measured() {
        val window = monitor.openWindow()
        time += 1.seconds

        val stats = monitor.closeWindow(window)

        assertFalse(stats.isMeasured)
        assertEquals(0, stats.averageFps)
    }

    @Test
    fun snapshot_is_published_every_quarter_second() {
        repeat(15) { monitor.onFrame(it * FRAME_NANOS_60_HZ) }
        assertEquals(PerfSnapshot(), monitor.snapshot.value)

        monitor.onLag(7.milliseconds)
        monitor.onFrame(15 * FRAME_NANOS_60_HZ)

        assertEquals(PerfSnapshot(fps = 60, worstFrameMs = 16, lagMs = 7), monitor.snapshot.value)
    }

    @Test
    fun snapshot_shows_blocked_main_thread() {
        frames(0, 16, 2016)

        assertEquals(1, monitor.snapshot.value.fps)
        assertEquals(2000, monitor.snapshot.value.worstFrameMs)
    }

    @Test
    fun reset_frame_clock_drops_gap_between_measurements() {
        val window = monitor.openWindow()
        frames(0, 16)
        monitor.resetFrameClock()

        frames(10_000, 10_016)

        val stats = monitor.closeWindow(window)
        assertEquals(2, stats.frames)
        assertEquals(16.milliseconds, stats.worstFrame)
    }

    @Test
    fun measuring_flag_follows_open_windows() = runTest {
        assertFalse(monitor.isMeasuring.first())
        val first = monitor.openWindow()
        val second = monitor.openWindow()
        assertTrue(monitor.isMeasuring.first())

        monitor.closeWindow(first)
        assertTrue(monitor.isMeasuring.first())
        monitor.closeWindow(second)
        assertFalse(monitor.isMeasuring.first())
    }

    @Test
    fun windows_are_independent() {
        val early = monitor.openWindow()
        frames(0, 100)
        val late = monitor.openWindow()
        frames(116)

        assertEquals(2, monitor.closeWindow(early).frames)
        assertEquals(Duration.parse("16ms"), monitor.closeWindow(late).worstFrame)
    }

    private companion object {
        const val NANOS_IN_MILLI = 1_000_000L
        const val FRAME_NANOS_60_HZ = 16_666_667L
    }
}
