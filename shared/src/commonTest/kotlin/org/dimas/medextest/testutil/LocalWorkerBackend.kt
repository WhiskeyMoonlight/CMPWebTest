package org.dimas.medextest.testutil

import org.dimas.medextest.compute.job.ComputeJob
import org.dimas.medextest.compute.job.ComputeOutcome
import org.dimas.medextest.compute.job.bytesHashStepper
import org.dimas.medextest.compute.job.runToEnd
import org.dimas.medextest.compute.job.stepper
import org.dimas.medextest.core.compute.ProgressSink
import org.dimas.medextest.core.compute.WorkerBackend
import org.dimas.medextest.core.media.PickedFile
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class LocalWorkerBackend(override val poolSize: Int = 2) : WorkerBackend {
    val executedJobs = mutableListOf<ComputeJob>()
    val hashedFiles = mutableListOf<String>()

    override val lastStartup: Duration = 5.milliseconds

    override suspend fun execute(job: ComputeJob, progress: ProgressSink): ComputeOutcome {
        executedJobs += job
        progress.advance(job.totalSteps)
        return job.stepper().runToEnd()
    }

    override suspend fun hashFile(file: PickedFile, progress: ProgressSink): ComputeOutcome {
        hashedFiles += file.name
        val bytes = file.readBytes()
        val stepper = bytesHashStepper(bytes)
        return stepper.runToEnd().also { progress.advance(1) }
    }
}
