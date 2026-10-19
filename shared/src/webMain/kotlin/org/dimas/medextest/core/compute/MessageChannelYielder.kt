package org.dimas.medextest.core.compute

import js.reflect.unsafeCast
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import web.messaging.MessageChannel
import web.messaging.MessageEvent
import kotlin.coroutines.resume
import kotlin.js.JsAny

class MessageChannelYielder : UiYielder {
    private val channel = MessageChannel()
    private val waiting = ArrayDeque<CancellableContinuation<Unit>>()

    init {
        val onMessage: (MessageEvent<JsAny?>) -> Unit = { waiting.removeFirstOrNull()?.resume(Unit) }
        channel.port1.onmessage = unsafeCast(onMessage)
    }

    override suspend fun yieldToUi() = suspendCancellableCoroutine { continuation ->
        waiting.addLast(continuation)
        channel.port2.postMessage(null)
    }
}
