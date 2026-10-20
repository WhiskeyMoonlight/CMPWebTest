package org.dimas.medextest.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import org.dimas.medextest.core.perf.PerfSnapshot
import org.dimas.medextest.core.perf.ui.PERF_HUD_TAG
import org.dimas.medextest.core.perf.ui.PerfHud
import org.dimas.medextest.core.perf.ui.RunningTasksSummary
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class PerfHudUiTest {
    @Test
    fun hud_draws_lines_without_text_semantics() = runComposeUiTest {
        setContent {
            PerfHud(
                snapshot = { PerfSnapshot(fps = 58, worstFrameMs = 34, lagMs = 12) },
                running = { RunningTasksSummary(count = 2, progress = 0.5f) },
            )
        }

        onNodeWithTag(PERF_HUD_TAG).assertExists()
        onAllNodesWithText("к/с", substring = true).assertCountEquals(0)
    }

    @Test
    fun hud_grows_by_task_line_while_tasks_run() = runComposeUiTest {
        var running by mutableStateOf<RunningTasksSummary?>(null)
        setContent { PerfHud(snapshot = { PerfSnapshot(fps = 120, worstFrameMs = 9, lagMs = 1) }, running = { running }) }
        val idleHeight = onNodeWithTag(PERF_HUD_TAG).fetchSemanticsNode().size.height

        running = RunningTasksSummary(count = 1, progress = 0f)
        waitForIdle()

        val busyHeight = onNodeWithTag(PERF_HUD_TAG).fetchSemanticsNode().size.height
        assertTrue(busyHeight > idleHeight, "idle=$idleHeight busy=$busyHeight")
    }
}
