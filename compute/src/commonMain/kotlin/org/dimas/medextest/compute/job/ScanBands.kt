package org.dimas.medextest.compute.job

object ScanBands {
    fun split(width: Int, height: Int, seed: Int, radius: Int, bands: Int): List<ScanBandJob> {
        val count = bands.coerceIn(1, height)
        return List(count) { index ->
            ScanBandJob(
                width = width,
                height = height,
                seed = seed,
                radius = radius,
                rowFrom = height * index / count,
                rowTo = height * (index + 1) / count,
            )
        }
    }
}
