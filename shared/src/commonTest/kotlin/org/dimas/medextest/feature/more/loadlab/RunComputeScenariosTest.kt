package org.dimas.medextest.feature.more.loadlab

import kotlinx.coroutines.test.runTest
import org.dimas.medextest.core.background.TaskProgress
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunAttachmentHashUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunScanFilterUseCase
import org.dimas.medextest.testutil.LocalWorkerBackend
import org.dimas.medextest.testutil.testExecutor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RunComputeScenariosTest {
    @Test
    fun attachment_hashes_match_across_modes() = runTest {
        val hashes = ExecutionMode.entries.associateWith { mode ->
            val useCase = RunAttachmentHashUseCase(
                executor = testExecutor(workers = LocalWorkerBackend()),
                files = 3,
                fileSize = 100_000,
                timeSource = testScheduler.timeSource,
            )
            useCase(mode, LoadScale.X1, TaskProgress()).substringAfter("sha256 ")
        }

        assertEquals(1, hashes.values.toSet().size, hashes.toString())
    }

    @Test
    fun attachment_summary_describes_batch() = runTest {
        val progress = TaskProgress()
        val useCase = RunAttachmentHashUseCase(testExecutor(), files = 2, fileSize = 1024, timeSource = testScheduler.timeSource)

        val summary = useCase(ExecutionMode.Default, LoadScale.X4, progress)

        assertTrue(summary.startsWith("8 × 1"), summary)
        assertEquals(1f, progress.fraction)
    }

    @Test
    fun scan_filter_checksum_does_not_depend_on_banding() = runTest {
        val summaries = ExecutionMode.entries.associateWith { mode ->
            val useCase = RunScanFilterUseCase(
                executor = testExecutor(workers = LocalWorkerBackend(poolSize = 3), cpuCores = 5),
                width = 90,
                height = 50,
                radius = 3,
            )
            useCase(mode, LoadScale.X4, TaskProgress())
        }

        assertEquals(1, summaries.values.toSet().size, summaries.toString())
        assertTrue(summaries.getValue(ExecutionMode.Parallel).startsWith("4 × 90×50"))
    }

    @Test
    fun scan_filter_handles_more_threads_than_rows() = runTest {
        val useCase = RunScanFilterUseCase(testExecutor(cpuCores = 16), width = 10, height = 3, radius = 1)

        val parallel = useCase(ExecutionMode.Parallel, LoadScale.X1, TaskProgress())
        val sequential = useCase(ExecutionMode.Default, LoadScale.X1, TaskProgress())

        assertEquals(sequential, parallel)
    }
}
