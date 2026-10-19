package org.dimas.medextest.feature.more.loadlab.domain.usecase

import org.dimas.medextest.compute.job.ComputeOutcome
import org.dimas.medextest.compute.job.HashChunk
import org.dimas.medextest.compute.job.bytesHashStepper
import org.dimas.medextest.core.background.TaskProgress
import org.dimas.medextest.core.compute.ComputeExecutor
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.format.formatBytes
import org.dimas.medextest.core.format.formatDuration
import org.dimas.medextest.core.media.PickedFile
import kotlin.time.TimeSource

class HashPickedFileUseCase(
    private val executor: ComputeExecutor,
    private val timeSource: TimeSource = TimeSource.Monotonic,
) {
    suspend operator fun invoke(file: PickedFile, mode: ExecutionMode, progress: TaskProgress): String {
        require(file.sizeBytes <= Int.MAX_VALUE) { "Файл больше 2 ГБ" }
        progress.expect(HashChunk.stepsFor(file.sizeBytes.toInt()))
        val outcome: ComputeOutcome.Hash
        val readLabel: String
        if (mode == ExecutionMode.Worker) {
            outcome = executor.hashFileInWorker(file, progress) as ComputeOutcome.Hash
            readLabel = "передача в воркер без копирования"
        } else {
            val readStarted = timeSource.markNow()
            val bytes = file.readBytes()
            readLabel = "чтение ${formatDuration(readStarted.elapsedNow())}"
            outcome = executor.execute(bytesHashStepper(bytes), mode, progress) as ComputeOutcome.Hash
        }
        return "${file.name} · ${formatBytes(file.sizeBytes)} · $readLabel · sha256 ${outcome.sha256.take(HASH_PREFIX)}…"
    }

    private companion object {
        const val HASH_PREFIX = 16
    }
}
