package org.dimas.medextest.core.background

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.dimas.medextest.core.perf.PerfMonitor
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

@OptIn(ExperimentalAtomicApi::class)
class BackgroundTasks(
    private val scope: CoroutineScope,
    private val perfMonitor: PerfMonitor,
    private val timeSource: TimeSource = TimeSource.Monotonic,
    private val progressInterval: Duration = DEFAULT_PROGRESS_INTERVAL,
    private val perfSettle: Duration = DEFAULT_PERF_SETTLE,
) {
    private val state = MutableStateFlow<List<BackgroundTask>>(emptyList())
    private val jobs = MutableStateFlow<Map<Int, Job>>(emptyMap())
    private val ids = AtomicInt(0)

    val tasks: StateFlow<List<BackgroundTask>> = state.asStateFlow()

    fun launch(title: String, details: String, work: suspend (TaskProgress) -> String): Int {
        val id = ids.addAndFetch(1)
        state.update { tasks -> listOf(BackgroundTask(id = id, title = title, details = details)) + tasks }
        val job = scope.launch(start = CoroutineStart.LAZY) { run(id, work) }
        jobs.update { it + (id to job) }
        job.start()
        return id
    }

    fun cancel(id: Int) {
        jobs.value[id]?.cancel()
    }

    fun cancelAll() {
        jobs.value.values.forEach(Job::cancel)
    }

    fun clearFinished() {
        state.update { tasks -> tasks.filter { it.status == TaskStatus.Running } }
        val running = state.value.map(BackgroundTask::id).toSet()
        jobs.update { it.filterKeys(running::contains) }
    }

    private suspend fun run(id: Int, work: suspend (TaskProgress) -> String) = coroutineScope {
        val progress = TaskProgress()
        val window = perfMonitor.openWindow()
        val started = timeSource.markNow()
        val ticker = launch {
            while (true) {
                delay(progressInterval)
                updateTask(id) { it.copy(progress = progress.fraction, elapsed = started.elapsedNow()) }
            }
        }
        var cancellation: CancellationException? = null
        val status = try {
            TaskStatus.Completed(work(progress))
        } catch (error: CancellationException) {
            if (isActive) {
                TaskStatus.Failed(error.message ?: "Операция прервана")
            } else {
                cancellation = error
                TaskStatus.Cancelled
            }
        } catch (error: Exception) {
            TaskStatus.Failed(error.message ?: error::class.simpleName ?: "Ошибка")
        } finally {
            ticker.cancel()
        }
        val elapsed = started.elapsedNow()
        withContext(NonCancellable) { delay(perfSettle) }
        val perf = perfMonitor.closeWindow(window)
        updateTask(id) { task ->
            task.copy(
                status = status,
                progress = if (status is TaskStatus.Completed) 1f else progress.fraction,
                elapsed = elapsed,
                perf = perf,
            )
        }
        cancellation?.let { throw it }
    }

    private fun updateTask(id: Int, transform: (BackgroundTask) -> BackgroundTask) {
        state.update { tasks -> tasks.map { task -> if (task.id == id) transform(task) else task } }
    }

    companion object {
        val DEFAULT_PROGRESS_INTERVAL: Duration = 100.milliseconds
        val DEFAULT_PERF_SETTLE: Duration = 120.milliseconds
    }
}
