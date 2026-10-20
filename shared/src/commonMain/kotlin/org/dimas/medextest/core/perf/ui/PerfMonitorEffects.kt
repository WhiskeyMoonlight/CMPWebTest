package org.dimas.medextest.core.perf.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.dimas.medextest.core.perf.PerfMonitor
import org.dimas.medextest.core.perf.awaitDisplayFrame
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

private val LAG_PROBE_INTERVAL = 50.milliseconds

@Composable
fun PerfMonitorEffects(monitor: PerfMonitor) {
    LaunchedEffect(monitor) {
        launch {
            monitor.resetFrameClock()
            while (true) monitor.onFrame(awaitDisplayFrame())
        }
        launch {
            while (true) {
                val mark = TimeSource.Monotonic.markNow()
                delay(LAG_PROBE_INTERVAL)
                monitor.onLag(mark.elapsedNow() - LAG_PROBE_INTERVAL)
            }
        }
    }
}
