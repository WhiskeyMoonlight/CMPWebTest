package org.dimas.medextest.feature.more.loadlab

import org.dimas.medextest.core.background.BackgroundTask
import org.dimas.medextest.core.background.TaskStatus
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.format.NBSP
import org.dimas.medextest.core.perf.FrameWindowStats
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUiStatus
import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabState
import org.dimas.medextest.feature.more.loadlab.presentation.util.label
import org.dimas.medextest.feature.more.loadlab.presentation.util.toMarkdownReport
import org.dimas.medextest.feature.more.loadlab.presentation.util.toUi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class LoadLabPresentationTest {
    private val stats = FrameWindowStats(
        frames = 120,
        slowFrames = 3,
        frozenFrames = 1,
        worstFrame = 900.milliseconds,
        worstLag = 880.milliseconds,
        duration = 2.seconds,
    )

    @Test
    fun completed_task_shows_summary_and_frame_stats() {
        val ui = BackgroundTask(1, "Импорт", "Default · ×1", TaskStatus.Completed("ok"), 1f, 1500.milliseconds, stats).toUi()

        assertEquals(TaskUiStatus.Completed, ui.status)
        assertEquals("ok", ui.result)
        assertEquals("1,50${NBSP}с", ui.elapsed)
        assertEquals("60 к/с · худший кадр 900${NBSP}мс · >17 мс: 3 · >700 мс: 1 · лаг UI до 880${NBSP}мс", ui.perf)
    }

    @Test
    fun failed_task_shows_reason_and_cancelled_has_no_result() {
        assertEquals("boom", BackgroundTask(1, "t", "d", TaskStatus.Failed("boom")).toUi().result)
        assertNull(BackgroundTask(1, "t", "d", TaskStatus.Cancelled).toUi().result)
    }

    @Test
    fun unmeasured_window_explains_missing_frames() {
        val label = stats.copy(frames = 0).label()

        assertTrue(label.startsWith("Кадры не измерялись"))
    }

    @Test
    fun report_lists_tasks_oldest_first() {
        val state = LoadLabState(
            platformName = "Chrome · Kotlin/Wasm",
            cpuCores = 10,
            workerPoolSize = 8,
            availableModes = ExecutionMode.entries,
            tasks = listOf(
                BackgroundTask(2, "Вторая", "Worker", TaskStatus.Completed("b")).toUi(),
                BackgroundTask(1, "Первая", "Default", TaskStatus.Completed("a")).toUi(),
            ),
        )

        val report = state.toMarkdownReport().lines()

        assertEquals("Платформа: Chrome · Kotlin/Wasm · ядер: 10 · воркеров: 8", report.first())
        assertTrue(report[4].startsWith("| Первая |"))
        assertTrue(report[5].startsWith("| Вторая |"))
    }
}
