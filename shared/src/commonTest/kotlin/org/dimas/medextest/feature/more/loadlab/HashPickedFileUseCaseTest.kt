package org.dimas.medextest.feature.more.loadlab

import kotlinx.coroutines.test.runTest
import org.dimas.medextest.compute.hash.Sha256
import org.dimas.medextest.compute.hash.toHex
import org.dimas.medextest.core.background.TaskProgress
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.media.PickedFile
import org.dimas.medextest.feature.more.loadlab.domain.usecase.HashPickedFileUseCase
import org.dimas.medextest.testutil.LocalWorkerBackend
import org.dimas.medextest.testutil.testExecutor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class HashPickedFileUseCaseTest {
    private val bytes = ByteArray(200_000) { (it * 31).toByte() }
    private val file = PickedFile(name = "анализы.pdf", mimeType = "application/pdf", sizeBytes = bytes.size.toLong(), reader = { bytes })
    private val expectedPrefix = Sha256().update(bytes).digest().toHex().take(16)

    @Test
    fun local_modes_read_file_and_hash_it() = runTest {
        listOf(ExecutionMode.Main, ExecutionMode.Default, ExecutionMode.Parallel, ExecutionMode.Cooperative).forEach { mode ->
            val progress = TaskProgress()
            val summary = HashPickedFileUseCase(testExecutor(), testScheduler.timeSource)(file, mode, progress)

            assertTrue(summary.startsWith("анализы.pdf · "), summary)
            assertTrue(summary.endsWith("sha256 $expectedPrefix…"), summary)
            assertTrue("чтение" in summary, summary)
            assertEquals(1f, progress.fraction)
        }
    }

    @Test
    fun worker_mode_hands_file_to_worker_pool() = runTest {
        val workers = LocalWorkerBackend()

        val summary = HashPickedFileUseCase(testExecutor(workers = workers))(file, ExecutionMode.Worker, TaskProgress())

        assertEquals(listOf("анализы.pdf"), workers.hashedFiles)
        assertTrue(summary.endsWith("sha256 $expectedPrefix…"), summary)
        assertTrue("без копирования" in summary, summary)
    }

    @Test
    fun files_over_two_gigabytes_are_rejected() = runTest {
        val huge = PickedFile(name = "huge.bin", mimeType = null, sizeBytes = Int.MAX_VALUE + 1L, reader = { ByteArray(0) })

        assertFailsWith<IllegalArgumentException> {
            HashPickedFileUseCase(testExecutor())(huge, ExecutionMode.Default, TaskProgress())
        }
    }
}
