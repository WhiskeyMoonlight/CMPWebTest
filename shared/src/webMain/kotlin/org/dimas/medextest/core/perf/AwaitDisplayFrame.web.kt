package org.dimas.medextest.core.perf

import js.numbers.JsNumbers.toKotlinDouble
import kotlinx.coroutines.suspendCancellableCoroutine
import web.animations.cancelAnimationFrame
import web.animations.requestAnimationFrame
import kotlin.coroutines.resume

private const val NANOS_IN_MILLI = 1_000_000.0

internal actual suspend fun awaitDisplayFrame(): Long = suspendCancellableCoroutine { continuation ->
    val request = requestAnimationFrame { time -> continuation.resume((time.toKotlinDouble() * NANOS_IN_MILLI).toLong()) }
    continuation.invokeOnCancellation { cancelAnimationFrame(request) }
}
