package org.dimas.medextest.compute.job

import org.dimas.medextest.compute.data.SeededByteStream
import org.dimas.medextest.compute.hash.Sha256
import org.dimas.medextest.compute.hash.toHex
import org.dimas.medextest.compute.lab.LabPageGenerator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class StepperTest {
    private fun ComputeJob.countSteps(): Int {
        val stepper = stepper()
        var steps = 0
        while (!stepper.isDone) {
            stepper.step()
            steps++
        }
        return steps
    }

    @Test
    fun declared_total_steps_match_executed_steps() {
        val jobs = listOf(
            HashJob(seed = 1, sizeBytes = 0),
            HashJob(seed = 1, sizeBytes = 1),
            HashJob(seed = 1, sizeBytes = HashChunk.SIZE_BYTES),
            HashJob(seed = 1, sizeBytes = HashChunk.SIZE_BYTES * 3 + 17),
            LabParseJob(LabPageGenerator.json(page = 0, pageSize = 10, seed = 1)),
            ScanBandJob(width = 10, height = 10, seed = 1, radius = 2, rowFrom = 3, rowTo = 9),
        )

        jobs.forEach { job -> assertEquals(job.totalSteps, job.countSteps(), job.toString().take(60)) }
    }

    @Test
    fun seeded_hash_job_hashes_seeded_stream() {
        val size = HashChunk.SIZE_BYTES * 2 + 5
        val bytes = ByteArray(size).also { SeededByteStream(9).fill(it) }

        val outcome = HashJob(seed = 9, sizeBytes = size).stepper().runToEnd()

        assertEquals(ComputeOutcome.Hash(size.toLong(), Sha256().update(bytes).digest().toHex()), outcome)
    }

    @Test
    fun bytes_hash_stepper_matches_direct_hash() {
        val bytes = ByteArray(HashChunk.SIZE_BYTES + 100) { it.toByte() }

        val outcome = bytesHashStepper(bytes).runToEnd()

        assertEquals(ComputeOutcome.Hash(bytes.size.toLong(), Sha256().update(bytes).digest().toHex()), outcome)
    }

    @Test
    fun empty_bytes_are_hashed_without_steps() {
        val stepper = bytesHashStepper(ByteArray(0))

        assertTrue(stepper.isDone)
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", (stepper.outcome() as ComputeOutcome.Hash).sha256)
    }

    @Test
    fun outcome_before_completion_fails() {
        val stepper = HashJob(seed = 1, sizeBytes = 10).stepper()

        assertFailsWith<IllegalStateException> { stepper.outcome() }
    }

    @Test
    fun scan_band_rejects_rows_outside_image() {
        assertFailsWith<IllegalArgumentException> {
            ScanBandJob(width = 10, height = 10, seed = 1, radius = 1, rowFrom = 5, rowTo = 11)
        }
    }
}
