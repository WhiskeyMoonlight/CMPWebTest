package org.dimas.medextest.core.compute

import js.objects.unsafeJso
import kotlinx.coroutines.channels.Channel
import org.dimas.medextest.compute.job.ComputeCodec
import org.dimas.medextest.compute.job.ComputeJob
import org.dimas.medextest.compute.job.ComputeOutcome
import org.dimas.medextest.compute.job.LabParseJob
import org.dimas.medextest.compute.worker.WorkerMessageKind
import org.dimas.medextest.compute.worker.WorkerRequest
import org.dimas.medextest.core.media.PickedFile
import kotlin.time.Duration
import kotlin.time.TimeSource

class WebWorkerBackend(
    private val scriptUrl: String,
    override val poolSize: Int,
    private val timeSource: TimeSource = TimeSource.Monotonic,
) : WorkerBackend {
    private val idle = Channel<PooledWorker>(Channel.UNLIMITED, onUndeliveredElement = ::returnToPool)
    private var spawned = 0
    private var nextRequestId = 0

    override var lastStartup: Duration? = null
        private set

    override suspend fun execute(job: ComputeJob, progress: ProgressSink): ComputeOutcome {
        val request = when (job) {
            is LabParseJob -> request(WorkerMessageKind.LAB_PAGE, payload = job.json)
            else -> request(WorkerMessageKind.JOB, payload = ComputeCodec.encodeJob(job))
        }
        return ComputeCodec.decodeOutcome(withWorker { worker -> worker.run(request, transfer = null, progress) })
    }

    override suspend fun hashFile(file: PickedFile, progress: ProgressSink): ComputeOutcome {
        val buffer = file.arrayBuffer() ?: throw WorkerException("Нет доступа к содержимому файла «${file.name}»")
        val request = request(WorkerMessageKind.BYTES, payload = null).apply { this.buffer = buffer }
        return ComputeCodec.decodeOutcome(withWorker { worker -> worker.run(request, transfer = buffer, progress) })
    }

    private fun request(kind: String, payload: String?): WorkerRequest = unsafeJso {
        id = ++nextRequestId
        this.kind = kind
        this.payload = payload
        buffer = null
    }

    private suspend fun <T> withWorker(block: suspend (PooledWorker) -> T): T {
        val worker = acquire()
        val result = try {
            lastStartup = worker.awaitReady()
            block(worker)
        } catch (error: Throwable) {
            worker.terminate()
            returnToPool(spawn())
            throw error
        }
        returnToPool(worker)
        return result
    }

    private suspend fun acquire(): PooledWorker {
        idle.tryReceive().getOrNull()?.let { return it }
        if (spawned < poolSize) {
            spawned++
            return spawn()
        }
        return idle.receive()
    }

    private fun spawn(): PooledWorker = PooledWorker(scriptUrl, timeSource)

    private fun returnToPool(worker: PooledWorker) {
        idle.trySend(worker)
    }
}
