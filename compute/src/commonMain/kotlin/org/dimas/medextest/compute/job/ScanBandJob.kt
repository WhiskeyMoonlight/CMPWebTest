package org.dimas.medextest.compute.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("scan")
data class ScanBandJob(
    val width: Int,
    val height: Int,
    val seed: Int,
    val radius: Int,
    val rowFrom: Int,
    val rowTo: Int,
) : ComputeJob {
    init {
        require(width > 0 && height > 0) { "Image must not be empty" }
        require(radius >= 0) { "Radius must not be negative" }
        require(rowFrom in 0..rowTo && rowTo <= height) { "Rows $rowFrom..$rowTo are outside 0..$height" }
    }

    override val totalSteps: Int get() = rowTo - rowFrom
}
