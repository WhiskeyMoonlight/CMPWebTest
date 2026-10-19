package org.dimas.medextest.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import org.dimas.medextest.core.perf.PerfSnapshot
import org.dimas.medextest.core.perf.ui.PerfHud
import org.dimas.medextest.core.perf.ui.RunningTasksSummary
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class PerfHudUiTest {
    @Test
    fun hud_shows_frames_lag_and_running_tasks() = runComposeUiTest {
        setContent {
            PerfHud(
                snapshot = PerfSnapshot(fps = 58, worstFrameMs = 34, lagMs = 12),
                running = RunningTasksSummary(count = 2, progress = 0.5f),
            )
        }

        onNodeWithText("58 к/с · кадр 34 мс").assertExists()
        onNodeWithText("лаг 12 мс").assertExists()
        onNodeWithText("задач: 2 · 50%").assertExists()
    }

    @Test
    fun hud_hides_task_line_when_nothing_runs() = runComposeUiTest {
        setContent { PerfHud(snapshot = PerfSnapshot(fps = 120, worstFrameMs = 9, lagMs = 1), running = null) }

        onNodeWithText("120 к/с · кадр 9 мс").assertExists()
        onNodeWithText("задач", substring = true).assertDoesNotExist()
    }
}
