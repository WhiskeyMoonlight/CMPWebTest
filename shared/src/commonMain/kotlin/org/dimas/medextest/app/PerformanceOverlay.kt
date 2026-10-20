package org.dimas.medextest.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.dimas.medextest.core.background.BackgroundTasks
import org.dimas.medextest.core.perf.PerfHudPreference
import org.dimas.medextest.core.perf.PerfMonitor
import org.dimas.medextest.core.perf.ui.PerfHud
import org.dimas.medextest.core.perf.ui.PerfMonitorEffects
import org.dimas.medextest.core.perf.ui.RunningTasksSummary
import org.koin.compose.koinInject

@Composable
internal fun PerformanceOverlay(measure: Boolean, modifier: Modifier = Modifier) {
    val monitor = koinInject<PerfMonitor>()
    val hudPreference = koinInject<PerfHudPreference>()
    val backgroundTasks = koinInject<BackgroundTasks>()
    val hudEnabled by hudPreference.isEnabled.collectAsState()
    val measuring by monitor.isMeasuring.collectAsState(initial = false)

    if (measure && (hudEnabled || measuring)) PerfMonitorEffects(monitor)
    if (hudEnabled) {
        val snapshot = monitor.snapshot.collectAsState()
        val tasks = backgroundTasks.tasks.collectAsState()
        PerfHud(
            snapshot = { snapshot.value },
            running = { RunningTasksSummary.of(tasks.value) },
            modifier = modifier,
        )
    }
}
