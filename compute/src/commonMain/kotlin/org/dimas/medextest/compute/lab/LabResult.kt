package org.dimas.medextest.compute.lab

import kotlinx.serialization.Serializable

@Serializable
data class LabResult(
    val id: Long,
    val patientId: Int,
    val code: String,
    val value: Double,
    val unit: String,
    val takenAt: String,
    val laboratory: String,
    val comment: String? = null,
)
