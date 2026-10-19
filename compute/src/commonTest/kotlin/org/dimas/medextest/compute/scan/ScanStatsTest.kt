package org.dimas.medextest.compute.scan

import org.dimas.medextest.compute.job.ComputeOutcome
import org.dimas.medextest.compute.job.ScanBands
import org.dimas.medextest.compute.job.runToEnd
import org.dimas.medextest.compute.job.stepper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ScanStatsTest {
    private fun process(bands: Int, radius: Int = 3): ScanStats =
        ScanBands.split(width = 97, height = 61, seed = 4, radius = radius, bands = bands)
            .map { job -> assertIs<ComputeOutcome.Scan>(job.stepper().runToEnd()).stats }
            .fold(ScanStats.EMPTY, ScanStats::merge)

    @Test
    fun banded_processing_matches_whole_image() {
        val whole = process(bands = 1)

        listOf(2, 3, 7, 61).forEach { bands -> assertEquals(whole, process(bands), "bands = $bands") }
    }

    @Test
    fun histogram_counts_every_pixel() {
        val stats = process(bands = 4)

        assertEquals(97L * 61, stats.pixels)
        assertEquals(97 * 61, stats.histogram.sum())
    }

    @Test
    fun zero_radius_keeps_source_pixels() {
        val stats = process(bands = 3, radius = 0)
        val expected = IntArray(ScanStats.LEVELS)
        for (y in 0 until 61) for (x in 0 until 97) expected[ScanPixels.value(x, y, seed = 4)]++

        assertEquals(expected.toList(), stats.histogram)
    }

    @Test
    fun otsu_threshold_separates_two_peaks() {
        val histogram = List(ScanStats.LEVELS) { level -> if (level == 40 || level == 200) 1_000 else 0 }

        val threshold = ScanStats(pixels = 2_000, histogram = histogram).otsuThreshold()

        assertEquals(40, threshold)
    }
}
