package org.dimas.medextest.compute.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("hash")
data class HashJob(val seed: Int, val sizeBytes: Int) : ComputeJob {
    override val totalSteps: Int get() = HashChunk.stepsFor(sizeBytes)
}
