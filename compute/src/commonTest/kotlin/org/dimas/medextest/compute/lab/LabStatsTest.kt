package org.dimas.medextest.compute.lab

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LabStatsTest {
    @Test
    fun generated_page_round_trips_through_json() {
        val records = LabPageGenerator.records(page = 3, pageSize = 50, seed = 1)
        val json = LabPageGenerator.json(page = 3, pageSize = 50, seed = 1)

        assertEquals(records, LabJson.parsePage(json))
    }

    @Test
    fun generator_is_deterministic_per_page() {
        assertEquals(LabPageGenerator.json(1, 20, 9), LabPageGenerator.json(1, 20, 9))
        assertTrue(LabPageGenerator.json(1, 20, 9) != LabPageGenerator.json(2, 20, 9))
    }

    @Test
    fun stats_count_every_record_and_out_of_range_values() {
        val results = listOf(
            result(code = "GLU", value = 5.0),
            result(code = "GLU", value = 7.5),
            result(code = "HGB", value = 100.0),
        )

        val stats = LabStats.of(results)

        assertEquals(3, stats.records)
        assertEquals(2, stats.outOfRange)
        assertEquals(AnalyteStats(count = 2, outOfRange = 1, sumCents = 1250, minCents = 500, maxCents = 750), stats.analytes["GLU"])
    }

    @Test
    fun merging_page_stats_equals_stats_of_all_records() {
        val pages = (0 until 6).map { LabPageGenerator.records(page = it, pageSize = 40, seed = 5) }
        val whole = LabStats.of(pages.flatten())

        val sequential = pages.map(LabStats::of).fold(LabStats.EMPTY, LabStats::merge)
        val reversed = pages.map(LabStats::of).reversed().fold(LabStats.EMPTY, LabStats::merge)

        assertEquals(whole, sequential)
        assertEquals(whole, reversed)
    }

    @Test
    fun generated_values_include_out_of_range_results() {
        val stats = LabStats.of(LabPageGenerator.records(page = 0, pageSize = 2_000, seed = 11))

        assertTrue(stats.outOfRange in 100..600, "out of range: ${stats.outOfRange}")
    }

    private fun result(code: String, value: Double) = LabResult(
        id = 0,
        patientId = 1,
        code = code,
        value = value,
        unit = "",
        takenAt = "2026-01-01T00:00:00Z",
        laboratory = "Лаб",
    )
}
