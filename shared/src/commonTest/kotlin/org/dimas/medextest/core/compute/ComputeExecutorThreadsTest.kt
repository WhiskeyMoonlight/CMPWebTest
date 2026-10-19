package org.dimas.medextest.core.compute

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.dimas.medextest.compute.job.HashJob
import org.dimas.medextest.compute.job.runToEnd
import org.dimas.medextest.compute.job.stepper
import org.dimas.medextest.core.background.TaskProgress
import kotlin.test.Test
import kotlin.test.assertEquals

class ComputeExecutorThreadsTest {
    @Test
    fun parallel_mode_on_real_threads_matches_sequential_result() = runTest {
        val jobs = List(12) { HashJob(seed = it, sizeBytes = 300_000) }
        val executor = ComputeExecutor(
            mainDispatcher = Dispatchers.Default,
            defaultDispatcher = Dispatchers.Default,
            uiYielder = CoroutineYielder(),
            workers = NoWorkerBackend,
            cpuCores = 8,
        )
        val progress = TaskProgress().apply { expect(jobs.sumOf { it.totalSteps }) }

        val outcomes = withContext(Dispatchers.Default) { executor.executeAll(jobs, ExecutionMode.Parallel, progress) }

        assertEquals(jobs.map { it.stepper().runToEnd() }, outcomes)
        assertEquals(jobs.sumOf { it.totalSteps }, progress.completedSteps)
        assertEquals(1f, progress.fraction)
    }
}
