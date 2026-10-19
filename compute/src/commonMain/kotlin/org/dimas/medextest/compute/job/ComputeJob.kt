package org.dimas.medextest.compute.job

import kotlinx.serialization.Serializable

@Serializable
sealed interface ComputeJob {
    val totalSteps: Int
}
