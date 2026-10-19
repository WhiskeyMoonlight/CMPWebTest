package org.dimas.medextest.compute.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("lab")
data class LabParseJob(val json: String) : ComputeJob {
    override val totalSteps: Int get() = STEPS

    companion object {
        const val STEPS = 2
    }
}
