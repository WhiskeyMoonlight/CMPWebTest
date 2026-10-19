package org.dimas.medextest.core.compute

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.dimas.medextest.compute.job.HashJob
import org.dimas.medextest.compute.job.LabParseJob
import org.dimas.medextest.compute.job.ScanBandJob
import org.dimas.medextest.compute.job.bytesHashStepper
import org.dimas.medextest.compute.job.runToEnd
import org.dimas.medextest.compute.job.stepper
import org.dimas.medextest.compute.lab.LabPageGenerator
import org.dimas.medextest.testutil.CountingDispatcher
import org.dimas.medextest.testutil.CountingYielder
import org.dimas.medextest.testutil.LocalWorkerBackend
import org.dimas.medextest.testutil.RecordingProgress
import org.dimas.medextest.testutil.testExecutor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

class ComputeExecutorTest {
    private val jobs = listOf(
        HashJob(seed = 3, sizeBytes = 200_000),
        ScanBandJob(width = 64, height = 40, seed = 2, radius = 2, rowFrom = 0, rowTo = 40),
        LabParseJob(LabPageGenerator.json(page = 0, pageSize = 30, seed = 1)),
    )
    private val expected = jobs.map { it.stepper().runToEnd() }
    private val totalSteps = jobs.sumOf { it.totalSteps }

    @Test
    fun every_mode_produces_the_same_outcomes() = runTest {
        val executor = testExecutor(workers = LocalWorkerBackend())

        ExecutionMode.entries.forEach { mode ->
            assertEquals(expected, executor.executeAll(jobs, mode, RecordingProgress()), "mode $mode")
        }
    }

    @Test
    fun local_modes_report_every_step() = runTest {
        val executor = testExecutor()

        listOf(ExecutionMode.Main, ExecutionMode.Default, ExecutionMode.Parallel, ExecutionMode.Cooperative).forEach { mode ->
            val progress = RecordingProgress()
            executor.executeAll(jobs, mode, progress)
            assertEquals(totalSteps, progress.steps, "mode $mode")
        }
    }

    @Test
    fun cooperative_mode_yields_after_every_exhausted_slice() = runTest {
        val yielder = CountingYielder()
        val executor = testExecutor(yielder = yielder, sliceBudget = Duration.ZERO)

        executor.executeAll(jobs, ExecutionMode.Cooperative, RecordingProgress())

        assertEquals(totalSteps, yielder.yields)
    }

    @Test
    fun cooperative_mode_does_not_yield_within_budget() = runTest {
        val yielder = CountingYielder()
        val executor = testExecutor(yielder = yielder, sliceBudget = 1.hours)

        executor.executeAll(jobs, ExecutionMode.Cooperative, RecordingProgress())

        assertEquals(0, yielder.yields)
    }

    @Test
    fun blocking_modes_never_yield_to_ui() = runTest {
        val yielder = CountingYielder()
        val executor = testExecutor(yielder = yielder, sliceBudget = Duration.ZERO)

        listOf(ExecutionMode.Main, ExecutionMode.Default, ExecutionMode.Parallel).forEach { mode ->
            executor.executeAll(jobs, mode, RecordingProgress())
        }

        assertEquals(0, yielder.yields)
    }

    @Test
    fun main_and_cooperative_modes_run_on_main_dispatcher() = runTest {
        listOf(ExecutionMode.Main, ExecutionMode.Cooperative).forEach { mode ->
            val main = CountingDispatcher(StandardTestDispatcher(testScheduler))
            val default = CountingDispatcher(StandardTestDispatcher(testScheduler))
            val executor = testExecutor(mainDispatcher = main, defaultDispatcher = default)

            executor.execute(jobs.first(), mode, RecordingProgress())

            assertTrue(main.dispatches > 0, "mode $mode")
            assertEquals(0, default.dispatches, "mode $mode")
        }
    }

    @Test
    fun default_and_parallel_modes_run_on_default_dispatcher() = runTest {
        listOf(ExecutionMode.Default, ExecutionMode.Parallel).forEach { mode ->
            val main = CountingDispatcher(StandardTestDispatcher(testScheduler))
            val default = CountingDispatcher(StandardTestDispatcher(testScheduler))
            val executor = testExecutor(mainDispatcher = main, defaultDispatcher = default)

            executor.execute(jobs.first(), mode, RecordingProgress())

            assertEquals(0, main.dispatches, "mode $mode")
            assertTrue(default.dispatches > 0, "mode $mode")
        }
    }

    @Test
    fun cancellation_stops_blocking_loop_at_next_step() = runTest {
        val job = HashJob(seed = 1, sizeBytes = 64 * 1024 * 20)
        lateinit var running: Job
        val progress = RecordingProgress { steps -> if (steps == 3) running.cancel() }
        val executor = testExecutor()

        running = launch { executor.execute(job, ExecutionMode.Default, progress) }
        advanceUntilIdle()

        assertTrue(running.isCancelled)
        assertEquals(3, progress.steps)
    }

    @Test
    fun cancellation_during_ui_yield_stops_cooperative_loop() = runTest {
        val yielder = CountingYielder { yields -> if (yields == 4) throw CancellationException("cancelled from UI") }
        val executor = testExecutor(yielder = yielder, sliceBudget = Duration.ZERO)
        val progress = RecordingProgress()

        assertFailsWith<CancellationException> {
            executor.execute(HashJob(seed = 1, sizeBytes = 64 * 1024 * 20), ExecutionMode.Cooperative, progress)
        }

        assertEquals(4, progress.steps)
    }

    @Test
    fun worker_mode_delegates_every_job_to_backend() = runTest {
        val workers = LocalWorkerBackend(poolSize = 3)
        val executor = testExecutor(workers = workers)

        val outcomes = executor.executeAll(jobs, ExecutionMode.Worker, RecordingProgress())

        assertEquals(expected, outcomes)
        assertEquals(jobs.toSet(), workers.executedJobs.toSet())
    }

    @Test
    fun local_stepper_cannot_be_sent_to_worker() = runTest {
        val executor = testExecutor(workers = LocalWorkerBackend())

        assertFailsWith<IllegalArgumentException> {
            executor.execute(bytesHashStepper(ByteArray(10)), ExecutionMode.Worker, RecordingProgress())
        }
    }

    @Test
    fun worker_mode_is_available_only_with_worker_pool() = runTest {
        assertFalse(ExecutionMode.Worker in testExecutor().availableModes)
        assertTrue(ExecutionMode.Worker in testExecutor(workers = LocalWorkerBackend()).availableModes)
    }

    @Test
    fun concurrency_follows_mode_and_hardware() = runTest {
        val executor = testExecutor(workers = LocalWorkerBackend(poolSize = 7), cpuCores = 6)

        assertEquals(1, executor.concurrencyOf(ExecutionMode.Main))
        assertEquals(1, executor.concurrencyOf(ExecutionMode.Default))
        assertEquals(1, executor.concurrencyOf(ExecutionMode.Cooperative))
        assertEquals(6, executor.concurrencyOf(ExecutionMode.Parallel))
        assertEquals(7, executor.concurrencyOf(ExecutionMode.Worker))
    }
}
