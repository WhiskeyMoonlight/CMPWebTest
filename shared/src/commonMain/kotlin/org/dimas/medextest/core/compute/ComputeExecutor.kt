package org.dimas.medextest.core.compute

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.dimas.medextest.compute.job.ComputeJob
import org.dimas.medextest.compute.job.ComputeOutcome
import org.dimas.medextest.compute.job.Stepper
import org.dimas.medextest.compute.job.stepper
import org.dimas.medextest.core.media.PickedFile
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

class ComputeExecutor(
    private val mainDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher,
    private val uiYielder: UiYielder,
    private val workers: WorkerBackend,
    private val cpuCores: Int,
    private val sliceBudget: Duration = DEFAULT_SLICE_BUDGET,
    private val timeSource: TimeSource = TimeSource.Monotonic,
) {
    val availableModes: List<ExecutionMode> =
        ExecutionMode.entries.filter { mode -> mode != ExecutionMode.Worker || workers.poolSize > 0 }

    val workerPoolSize: Int get() = workers.poolSize

    val lastWorkerStartup: Duration? get() = workers.lastStartup

    fun concurrencyOf(mode: ExecutionMode): Int = when (mode) {
        ExecutionMode.Parallel -> cpuCores.coerceAtLeast(1)
        ExecutionMode.Worker -> workers.poolSize.coerceAtLeast(1)
        ExecutionMode.Main, ExecutionMode.Default, ExecutionMode.Cooperative -> 1
    }

    fun dispatcherFor(mode: ExecutionMode): CoroutineDispatcher = when (mode) {
        ExecutionMode.Main, ExecutionMode.Cooperative -> mainDispatcher
        ExecutionMode.Default, ExecutionMode.Parallel, ExecutionMode.Worker -> defaultDispatcher
    }

    suspend fun execute(job: ComputeJob, mode: ExecutionMode, progress: ProgressSink): ComputeOutcome =
        if (mode == ExecutionMode.Worker) workers.execute(job, progress) else execute(job.stepper(), mode, progress)

    suspend fun execute(stepper: Stepper, mode: ExecutionMode, progress: ProgressSink): ComputeOutcome = when (mode) {
        ExecutionMode.Main -> withContext(mainDispatcher) { runToEnd(stepper, progress) }
        ExecutionMode.Default, ExecutionMode.Parallel -> withContext(defaultDispatcher) { runToEnd(stepper, progress) }
        ExecutionMode.Cooperative -> withContext(mainDispatcher) { runSliced(stepper, progress) }
        ExecutionMode.Worker -> throw IllegalArgumentException("Локальный stepper нельзя передать в воркер")
    }

    suspend fun hashFileInWorker(file: PickedFile, progress: ProgressSink): ComputeOutcome = workers.hashFile(file, progress)

    suspend fun executeAll(jobs: List<ComputeJob>, mode: ExecutionMode, progress: ProgressSink): List<ComputeOutcome> {
        val concurrency = concurrencyOf(mode)
        if (concurrency == 1) return jobs.map { job -> execute(job, mode, progress) }
        val permits = Semaphore(concurrency)
        return coroutineScope {
            jobs.map { job -> async { permits.withPermit { execute(job, mode, progress) } } }.awaitAll()
        }
    }

    private suspend fun runToEnd(stepper: Stepper, progress: ProgressSink): ComputeOutcome {
        val context = currentCoroutineContext()
        while (!stepper.isDone) {
            context.ensureActive()
            stepper.step()
            progress.advance(1)
        }
        return stepper.outcome()
    }

    private suspend fun runSliced(stepper: Stepper, progress: ProgressSink): ComputeOutcome {
        var slice = timeSource.markNow()
        while (!stepper.isDone) {
            currentCoroutineContext().ensureActive()
            stepper.step()
            progress.advance(1)
            if (slice.elapsedNow() >= sliceBudget) {
                uiYielder.yieldToUi()
                slice = timeSource.markNow()
            }
        }
        return stepper.outcome()
    }

    companion object {
        val DEFAULT_SLICE_BUDGET: Duration = 6.milliseconds
    }
}
