package org.dimas.medextest.compute.job

import kotlinx.serialization.json.Json

object ComputeCodec {
    private val format = Json { classDiscriminator = "type" }

    fun encodeJob(job: ComputeJob): String = format.encodeToString(ComputeJob.serializer(), job)

    fun decodeJob(text: String): ComputeJob = format.decodeFromString(ComputeJob.serializer(), text)

    fun encodeOutcome(outcome: ComputeOutcome): String = format.encodeToString(ComputeOutcome.serializer(), outcome)

    fun decodeOutcome(text: String): ComputeOutcome = format.decodeFromString(ComputeOutcome.serializer(), text)
}
