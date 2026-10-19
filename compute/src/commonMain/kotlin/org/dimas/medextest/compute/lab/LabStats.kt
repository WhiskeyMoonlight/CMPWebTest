package org.dimas.medextest.compute.lab

import kotlinx.serialization.Serializable

@Serializable
data class LabStats(
    val records: Int = 0,
    val analytes: Map<String, AnalyteStats> = emptyMap(),
) {
    val outOfRange: Int get() = analytes.values.sumOf { it.outOfRange }

    fun merge(other: LabStats): LabStats {
        val merged = HashMap(analytes)
        other.analytes.forEach { (code, stats) -> merged[code] = merged[code]?.merge(stats) ?: stats }
        return LabStats(records = records + other.records, analytes = merged)
    }

    companion object {
        val EMPTY = LabStats()

        fun of(results: List<LabResult>): LabStats {
            val analytes = HashMap<String, AnalyteStats>()
            for (result in results) {
                val inRange = LabAnalyte.of(result.code)?.isInRange(result.value) ?: true
                val single = AnalyteStats.of(result.value, inRange)
                analytes[result.code] = analytes[result.code]?.merge(single) ?: single
            }
            return LabStats(records = results.size, analytes = analytes)
        }
    }
}
