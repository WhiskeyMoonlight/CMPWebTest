package org.dimas.medextest.core.perf.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import kotlinx.coroutines.delay
import org.dimas.medextest.core.perf.PerfMonitor
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

private val LAG_PROBE_INTERVAL = 50.milliseconds

@Composable
fun PerfMonitorEffects(monitor: PerfMonitor) {
    LaunchedEffect(monitor) {
        monitor.resetFrameClock()
        while (true) withFrameNanos(monitor::onFrame)
    }
    LaunchedEffect(monitor) {
        while (true) {
            val mark = TimeSource.Monotonic.markNow()
            delay(LAG_PROBE_INTERVAL)
            monitor.onLag(mark.elapsedNow() - LAG_PROBE_INTERVAL)
        }
    }
}
