package org.dimas.medextest.core.perf.ui

import org.dimas.medextest.core.perf.PerfSnapshot

private const val SMOOTH_FRAME_MS = 20
private const val NOTICEABLE_FRAME_MS = 50
private const val PERCENT = 100

fun perfHudLines(snapshot: PerfSnapshot, running: RunningTasksSummary?): List<HudLine> = buildList {
    add(HudLine("${snapshot.fps} к/с · кадр ${snapshot.worstFrameMs} мс", frameTone(snapshot.worstFrameMs)))
    add(HudLine("лаг ${snapshot.lagMs} мс", frameTone(snapshot.lagMs)))
    if (running != null) {
        add(HudLine("задач: ${running.count} · ${(running.progress * PERCENT).toInt()}%", HudTone.Neutral))
    }
}

fun frameTone(millis: Int): HudTone = when {
    millis <= SMOOTH_FRAME_MS -> HudTone.Good
    millis <= NOTICEABLE_FRAME_MS -> HudTone.Warning
    else -> HudTone.Bad
}
