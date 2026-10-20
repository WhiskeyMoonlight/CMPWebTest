package org.dimas.medextest.core.perf.ui

import org.dimas.medextest.core.perf.PerfSnapshot
import kotlin.test.Test
import kotlin.test.assertEquals

class PerfHudLinesTest {
    @Test
    fun lines_show_frames_lag_and_running_tasks() {
        val lines = perfHudLines(
            snapshot = PerfSnapshot(fps = 58, worstFrameMs = 34, lagMs = 12),
            running = RunningTasksSummary(count = 2, progress = 0.5f),
        )

        assertEquals(
            listOf(
                HudLine("58 к/с · кадр 34 мс", HudTone.Warning),
                HudLine("лаг 12 мс", HudTone.Good),
                HudLine("задач: 2 · 50%", HudTone.Neutral),
            ),
            lines,
        )
    }

    @Test
    fun task_line_is_absent_when_nothing_runs() {
        val lines = perfHudLines(snapshot = PerfSnapshot(fps = 120, worstFrameMs = 9, lagMs = 1), running = null)

        assertEquals(listOf("120 к/с · кадр 9 мс", "лаг 1 мс"), lines.map { it.text })
    }

    @Test
    fun tone_follows_frame_budget() {
        assertEquals(HudTone.Good, frameTone(20))
        assertEquals(HudTone.Warning, frameTone(21))
        assertEquals(HudTone.Warning, frameTone(50))
        assertEquals(HudTone.Bad, frameTone(51))
    }
}
