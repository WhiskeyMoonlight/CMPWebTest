package org.dimas.medextest.compute.scan

import kotlinx.serialization.Serializable

@Serializable
data class ScanStats(
    val pixels: Long = 0,
    val histogram: List<Int> = List(LEVELS) { 0 },
    val checksum: Int = 0,
) {
    fun merge(other: ScanStats): ScanStats = ScanStats(
        pixels = pixels + other.pixels,
        histogram = List(LEVELS) { level -> histogram[level] + other.histogram[level] },
        checksum = checksum + other.checksum,
    )

    fun otsuThreshold(): Int {
        val total = histogram.sumOf { it.toLong() }.toDouble()
        if (total == 0.0) return 0
        val weightedTotal = histogram.withIndex().sumOf { (level, count) -> level.toDouble() * count }
        var backgroundWeight = 0.0
        var backgroundSum = 0.0
        var bestVariance = -1.0
        var bestThreshold = 0
        for (level in 0 until LEVELS) {
            backgroundWeight += histogram[level]
            if (backgroundWeight == 0.0) continue
            val foregroundWeight = total - backgroundWeight
            if (foregroundWeight == 0.0) break
            backgroundSum += level.toDouble() * histogram[level]
            val backgroundMean = backgroundSum / backgroundWeight
            val foregroundMean = (weightedTotal - backgroundSum) / foregroundWeight
            val between = backgroundWeight * foregroundWeight * (backgroundMean - foregroundMean) * (backgroundMean - foregroundMean)
            if (between > bestVariance) {
                bestVariance = between
                bestThreshold = level
            }
        }
        return bestThreshold
    }

    companion object {
        const val LEVELS = 256
        val EMPTY = ScanStats()
    }
}
