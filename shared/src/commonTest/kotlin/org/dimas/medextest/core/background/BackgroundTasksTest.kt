package org.dimas.medextest.core.background

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.dimas.medextest.core.perf.PerfMonitor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class BackgroundTasksTest {
    private fun TestScope.backgroundTasks(monitor: PerfMonitor = PerfMonitor(testScheduler.timeSource)) = BackgroundTasks(
        scope = CoroutineScope(SupervisorJob() + StandardTestDispatcher(testScheduler)),
        perfMonitor = monitor,
        timeSource = testScheduler.timeSource,
    )

    private fun BackgroundTasks.task(id: Int): BackgroundTask = tasks.value.single { it.id == id }

    @Test
    fun completed_task_keeps_summary_full_progress_and_elapsed_time() = runTest {
        val tasks = backgroundTasks()

        val id = tasks.launch("Импорт", "Default") { progress ->
            progress.expect(1)
            delay(2.seconds)
            progress.advance(1)
            "готово"
        }
        advanceUntilIdle()

        val task = tasks.task(id)
        assertEquals(TaskStatus.Completed("готово"), task.status)
        assertEquals(1f, task.progress)
        assertEquals(2.seconds, task.elapsed)
        assertNotNull(task.perf)
    }

    @Test
    fun failure_is_reported_and_does_not_cancel_other_tasks() = runTest {
        val tasks = backgroundTasks()

        val failing = tasks.launch("Сбой", "") { delay(100.milliseconds); error("boom") }
        val healthy = tasks.launch("Норма", "") { delay(1.seconds); "ok" }
        advanceUntilIdle()

        assertEquals(TaskStatus.Failed("boom"), tasks.task(failing).status)
        assertEquals(TaskStatus.Completed("ok"), tasks.task(healthy).status)
    }

    @Test
    fun cancel_marks_only_that_task_cancelled() = runTest {
        val tasks = backgroundTasks()
        val cancelled = tasks.launch("Долгая", "") { delay(10.seconds); "never" }
        val other = tasks.launch("Другая", "") { delay(1.seconds); "ok" }
        runCurrent()

        tasks.cancel(cancelled)
        advanceUntilIdle()

        assertEquals(TaskStatus.Cancelled, tasks.task(cancelled).status)
        assertEquals(TaskStatus.Completed("ok"), tasks.task(other).status)
    }

    @Test
    fun timeout_inside_task_is_failure_not_cancellation() = runTest {
        val tasks = backgroundTasks()

        val id = tasks.launch("Таймаут", "") { withTimeout(1.seconds) { delay(5.seconds) }; "never" }
        advanceUntilIdle()

        assertIs<TaskStatus.Failed>(tasks.task(id).status)
    }

    @Test
    fun progress_is_published_while_task_runs() = runTest {
        val tasks = backgroundTasks()
        val id = tasks.launch("Шаги", "") { progress ->
            progress.expect(10)
            repeat(10) {
                delay(30.milliseconds)
                progress.advance(1)
            }
            "ok"
        }

        advanceTimeBy(260.milliseconds)
        runCurrent()

        val running = tasks.task(id)
        assertEquals(TaskStatus.Running, running.status)
        assertEquals(0.6f, running.progress)
        assertEquals(200.milliseconds, running.elapsed)
        advanceUntilIdle()
    }

    @Test
    fun newest_task_comes_first() = runTest {
        val tasks = backgroundTasks()

        val first = tasks.launch("Первая", "") { "1" }
        val second = tasks.launch("Вторая", "") { "2" }

        assertEquals(listOf(second, first), tasks.tasks.value.map { it.id })
        advanceUntilIdle()
    }

    @Test
    fun clear_finished_keeps_running_tasks() = runTest {
        val tasks = backgroundTasks()
        tasks.launch("Быстрая", "") { "ok" }
        val running = tasks.launch("Долгая", "") { delay(10.seconds); "ok" }
        advanceTimeBy(1.seconds)

        tasks.clearFinished()

        assertEquals(listOf(running), tasks.tasks.value.map { it.id })
        tasks.cancelAll()
        advanceUntilIdle()
        assertEquals(TaskStatus.Cancelled, tasks.task(running).status)
    }

    @Test
    fun perf_window_is_open_only_while_tasks_run() = runTest {
        val monitor = PerfMonitor(testScheduler.timeSource)
        val tasks = backgroundTasks(monitor)

        tasks.launch("Задача", "") { delay(1.seconds); "ok" }
        runCurrent()
        assertTrue(monitor.isMeasuring.first())

        advanceUntilIdle()
        assertFalse(monitor.isMeasuring.first())
    }
}
