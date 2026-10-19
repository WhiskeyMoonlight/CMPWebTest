package org.dimas.medextest.compute.job

import org.dimas.medextest.compute.lab.LabPageGenerator
import kotlin.test.Test
import kotlin.test.assertEquals

class ComputeCodecTest {
    @Test
    fun jobs_round_trip_through_codec() {
        val jobs = listOf(
            HashJob(seed = 5, sizeBytes = 1_000),
            ScanBandJob(width = 30, height = 20, seed = 2, radius = 1, rowFrom = 0, rowTo = 20),
            LabParseJob(LabPageGenerator.json(page = 1, pageSize = 3, seed = 1)),
        )

        jobs.forEach { job -> assertEquals(job, ComputeCodec.decodeJob(ComputeCodec.encodeJob(job))) }
    }

    @Test
    fun outcomes_round_trip_through_codec() {
        val outcomes = listOf(
            HashJob(seed = 5, sizeBytes = 1_000).stepper().runToEnd(),
            ScanBandJob(width = 30, height = 20, seed = 2, radius = 1, rowFrom = 0, rowTo = 20).stepper().runToEnd(),
            LabParseJob(LabPageGenerator.json(page = 1, pageSize = 30, seed = 1)).stepper().runToEnd(),
        )

        outcomes.forEach { outcome -> assertEquals(outcome, ComputeCodec.decodeOutcome(ComputeCodec.encodeOutcome(outcome))) }
    }

    @Test
    fun job_type_is_written_as_discriminator() {
        val encoded = ComputeCodec.encodeJob(HashJob(seed = 1, sizeBytes = 2))

        assertEquals("""{"type":"hash","seed":1,"sizeBytes":2}""", encoded)
    }
}
