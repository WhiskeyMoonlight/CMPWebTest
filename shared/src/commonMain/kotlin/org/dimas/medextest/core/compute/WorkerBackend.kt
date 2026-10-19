package org.dimas.medextest.core.compute

import org.dimas.medextest.compute.job.ComputeJob
import org.dimas.medextest.compute.job.ComputeOutcome
import org.dimas.medextest.core.media.PickedFile
import kotlin.time.Duration

interface WorkerBackend {
    val poolSize: Int

    val lastStartup: Duration?

    suspend fun execute(job: ComputeJob, progress: ProgressSink): ComputeOutcome

    suspend fun hashFile(file: PickedFile, progress: ProgressSink): ComputeOutcome
}
