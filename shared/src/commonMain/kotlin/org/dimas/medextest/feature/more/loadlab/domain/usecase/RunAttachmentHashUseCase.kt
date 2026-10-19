package org.dimas.medextest.feature.more.loadlab.domain.usecase

import org.dimas.medextest.compute.job.ComputeOutcome
import org.dimas.medextest.compute.job.HashJob
import org.dimas.medextest.core.background.TaskProgress
import org.dimas.medextest.core.compute.ComputeExecutor
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.format.formatBytes
import org.dimas.medextest.core.format.formatThroughput
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import kotlin.time.TimeSource

class RunAttachmentHashUseCase(
    private val executor: ComputeExecutor,
    private val files: Int = DEFAULT_FILES,
    private val fileSize: Int = DEFAULT_FILE_SIZE,
    private val timeSource: TimeSource = TimeSource.Monotonic,
) {
    suspend operator fun invoke(mode: ExecutionMode, scale: LoadScale, progress: TaskProgress): String {
        val jobs = List(files * scale.factor) { index -> HashJob(seed = index + 1, sizeBytes = fileSize) }
        progress.expect(jobs.sumOf { it.totalSteps })
        val started = timeSource.markNow()
        val outcomes = executor.executeAll(jobs, mode, progress).map { it as ComputeOutcome.Hash }
        val totalBytes = outcomes.sumOf { it.sizeBytes }
        return "${jobs.size} × ${formatBytes(fileSize.toLong())} · ${formatThroughput(totalBytes, started.elapsedNow())}" +
            " · sha256 ${outcomes.first().sha256.take(HASH_PREFIX)}…"
    }

    companion object {
        const val DEFAULT_FILES = 8
        const val DEFAULT_FILE_SIZE = 8 * 1024 * 1024
        private const val HASH_PREFIX = 12
    }
}
