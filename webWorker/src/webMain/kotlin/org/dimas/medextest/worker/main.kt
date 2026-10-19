package org.dimas.medextest.worker

import js.reflect.unsafeCast
import org.dimas.medextest.compute.worker.WorkerMessageKind
import web.messaging.MessageEvent
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny

@OptIn(ExperimentalWasmJsInterop::class)
fun main() {
    val runner = WorkerJobRunner(post = { response -> self.postMessage(response) })
    val onMessage: (MessageEvent<JsAny?>) -> Unit = { event -> runner.handle(checkNotNull(unsafeCast(event.data))) }
    self.onmessage = unsafeCast(onMessage)
    self.postMessage(workerResponse(id = 0, kind = WorkerMessageKind.READY))
}
