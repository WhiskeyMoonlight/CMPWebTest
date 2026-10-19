package org.dimas.medextest.core.compute

import js.array.jsArrayOf
import js.buffer.ArrayBuffer
import js.reflect.unsafeCast
import js.serialization.Transferable
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.suspendCancellableCoroutine
import org.dimas.medextest.compute.worker.WorkerMessageKind
import org.dimas.medextest.compute.worker.WorkerRequest
import org.dimas.medextest.compute.worker.WorkerResponse
import web.errors.ErrorEvent
import web.messaging.MessageEvent
import web.workers.Worker
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.JsAny
import kotlin.time.Duration
import kotlin.time.TimeSource

internal class PooledWorker(scriptUrl: String, timeSource: TimeSource) {
    private val worker = Worker(scriptUrl)
    private val created = timeSource.markNow()
    private val ready = CompletableDeferred<Duration>()
    private var pending: PendingRequest? = null

    init {
        val onMessage: (MessageEvent<JsAny?>) -> Unit = { event ->
            onResponse(checkNotNull(unsafeCast<WorkerResponse>(event.data)))
        }
        val onError: (ErrorEvent) -> Unit = { event ->
            fail(WorkerException("Воркер упал: ${event.message.ifEmpty { "скрипт не загрузился" }}"))
        }
        worker.onmessage = unsafeCast(onMessage)
        worker.onerror = unsafeCast(onError)
    }

    suspend fun awaitReady(): Duration = ready.await()

    suspend fun run(request: WorkerRequest, transfer: ArrayBuffer?, progress: ProgressSink): String {
        awaitReady()
        return suspendCancellableCoroutine { continuation ->
            pending = PendingRequest(request.id, continuation, progress)
            if (transfer == null) {
                worker.postMessage(request)
            } else {
                worker.postMessage(request, jsArrayOf<Transferable>(transfer))
            }
        }
    }

    fun terminate() {
        worker.terminate()
        pending = null
    }

    private fun onResponse(response: WorkerResponse) {
        when (response.kind) {
            WorkerMessageKind.READY -> ready.complete(created.elapsedNow())
            WorkerMessageKind.PROGRESS -> pending?.takeIf { it.id == response.id }?.progress?.advance(response.steps)
            WorkerMessageKind.DONE -> settle(response.id) { it.resume(checkNotNull(response.payload)) }
            WorkerMessageKind.ERROR -> settle(response.id) { it.resumeWithException(WorkerException(response.payload ?: "Ошибка в воркере")) }
        }
    }

    private fun settle(id: Int, resume: (CancellableContinuation<String>) -> Unit) {
        val request = pending?.takeIf { it.id == id } ?: return
        pending = null
        resume(request.continuation)
    }

    private fun fail(error: WorkerException) {
        ready.completeExceptionally(error)
        pending?.continuation?.resumeWithException(error)
        pending = null
    }

    private class PendingRequest(
        val id: Int,
        val continuation: CancellableContinuation<String>,
        val progress: ProgressSink,
    )
}
