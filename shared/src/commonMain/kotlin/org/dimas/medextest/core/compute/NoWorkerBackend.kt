package org.dimas.medextest.core.compute

import org.dimas.medextest.compute.job.ComputeJob
import org.dimas.medextest.compute.job.ComputeOutcome
import org.dimas.medextest.core.media.PickedFile
import kotlin.time.Duration

object NoWorkerBackend : WorkerBackend {
    override val poolSize: Int = 0

    override val lastStartup: Duration? = null

    override suspend fun execute(job: ComputeJob, progress: ProgressSink): ComputeOutcome = unsupported()

    override suspend fun hashFile(file: PickedFile, progress: ProgressSink): ComputeOutcome = unsupported()

    private fun unsupported(): Nothing = throw UnsupportedOperationException("Web Worker доступен только в браузере")
}
