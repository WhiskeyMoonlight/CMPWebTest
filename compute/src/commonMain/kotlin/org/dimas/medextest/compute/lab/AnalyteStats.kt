package org.dimas.medextest.compute.lab

import kotlinx.serialization.Serializable
import kotlin.math.roundToLong

@Serializable
data class AnalyteStats(
    val count: Int,
    val outOfRange: Int,
    val sumCents: Long,
    val minCents: Long,
    val maxCents: Long,
) {
    val mean: Double get() = if (count == 0) 0.0 else sumCents / CENTS / count

    fun merge(other: AnalyteStats): AnalyteStats = AnalyteStats(
        count = count + other.count,
        outOfRange = outOfRange + other.outOfRange,
        sumCents = sumCents + other.sumCents,
        minCents = minOf(minCents, other.minCents),
        maxCents = maxOf(maxCents, other.maxCents),
    )

    companion object {
        private const val CENTS = 100.0

        fun of(value: Double, inRange: Boolean): AnalyteStats {
            val cents = (value * CENTS).roundToLong()
            return AnalyteStats(count = 1, outOfRange = if (inRange) 0 else 1, sumCents = cents, minCents = cents, maxCents = cents)
        }
    }
}
