package org.dimas.medextest.worker

import js.typedarrays.Int8Array
import js.typedarrays.toByteArray
import org.dimas.medextest.compute.job.ComputeCodec
import org.dimas.medextest.compute.job.LabParseJob
import org.dimas.medextest.compute.job.Stepper
import org.dimas.medextest.compute.job.bytesHashStepper
import org.dimas.medextest.compute.job.stepper
import org.dimas.medextest.compute.worker.WorkerMessageKind
import org.dimas.medextest.compute.worker.WorkerRequest
import org.dimas.medextest.compute.worker.WorkerResponse
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

internal class WorkerJobRunner(private val post: (WorkerResponse) -> Unit) {
    fun handle(request: WorkerRequest) {
        val response = runCatching { ComputeCodec.encodeOutcome(run(request.id, stepperFor(request))) }.fold(
            onSuccess = { outcome -> workerResponse(request.id, WorkerMessageKind.DONE, payload = outcome) },
            onFailure = { error -> workerResponse(request.id, WorkerMessageKind.ERROR, payload = error.message ?: error::class.simpleName) },
        )
        post(response)
    }

    private fun stepperFor(request: WorkerRequest): Stepper = when (request.kind) {
        WorkerMessageKind.JOB -> ComputeCodec.decodeJob(checkNotNull(request.payload) { "Job without payload" }).stepper()
        WorkerMessageKind.LAB_PAGE -> LabParseJob(checkNotNull(request.payload) { "Lab page without payload" }).stepper()
        WorkerMessageKind.BYTES -> bytesHashStepper(Int8Array(checkNotNull(request.buffer) { "Bytes without buffer" }).toByteArray())
        else -> error("Unknown request kind ${request.kind}")
    }

    private fun run(id: Int, stepper: Stepper) = run {
        var pending = 0
        var lastReport = TimeSource.Monotonic.markNow()
        while (!stepper.isDone) {
            stepper.step()
            pending++
            if (lastReport.elapsedNow() >= PROGRESS_INTERVAL) {
                post(workerResponse(id, WorkerMessageKind.PROGRESS, steps = pending))
                pending = 0
                lastReport = TimeSource.Monotonic.markNow()
            }
        }
        if (pending > 0) post(workerResponse(id, WorkerMessageKind.PROGRESS, steps = pending))
        stepper.outcome()
    }

    private companion object {
        val PROGRESS_INTERVAL = 50.milliseconds
    }
}
