package org.dimas.medextest.compute.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.dimas.medextest.compute.lab.LabStats
import org.dimas.medextest.compute.scan.ScanStats

@Serializable
sealed interface ComputeOutcome {
    @Serializable
    @SerialName("hash")
    data class Hash(val sizeBytes: Long, val sha256: String) : ComputeOutcome

    @Serializable
    @SerialName("lab")
    data class Lab(val stats: LabStats) : ComputeOutcome

    @Serializable
    @SerialName("scan")
    data class Scan(val stats: ScanStats) : ComputeOutcome
}
