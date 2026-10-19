package org.dimas.medextest.feature.more.loadlab.domain.usecase

import org.dimas.medextest.compute.job.ComputeOutcome
import org.dimas.medextest.compute.job.ScanBands
import org.dimas.medextest.compute.scan.ScanStats
import org.dimas.medextest.core.background.TaskProgress
import org.dimas.medextest.core.compute.ComputeExecutor
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale

class RunScanFilterUseCase(
    private val executor: ComputeExecutor,
    private val width: Int = DEFAULT_WIDTH,
    private val height: Int = DEFAULT_HEIGHT,
    private val radius: Int = DEFAULT_RADIUS,
) {
    suspend operator fun invoke(mode: ExecutionMode, scale: LoadScale, progress: TaskProgress): String {
        val concurrency = executor.concurrencyOf(mode)
        val bands = if (concurrency == 1) 1 else concurrency * BANDS_PER_THREAD
        val frames = scale.factor
        val frameJobs = (0 until frames).map { frame -> ScanBands.split(width, height, seed = frame + 1, radius = radius, bands = bands) }
        val jobs = frameJobs.flatten()
        progress.expect(jobs.sumOf { it.totalSteps })
        val stats = executor.executeAll(jobs, mode, progress)
            .map { (it as ComputeOutcome.Scan).stats }
            .chunked(frameJobs.first().size) { frameBands -> frameBands.fold(ScanStats.EMPTY, ScanStats::merge) }
        val checksum = stats.fold(0) { total, frame -> total + frame.checksum }.toUInt().toString(HEX)
        return "$frames × ${width}×$height · порог Оцу ${stats.first().otsuThreshold()} · контр. сумма $checksum"
    }

    companion object {
        const val DEFAULT_WIDTH = 6_000
        const val DEFAULT_HEIGHT = 4_000
        const val DEFAULT_RADIUS = 5
        private const val BANDS_PER_THREAD = 2
        private const val HEX = 16
    }
}
