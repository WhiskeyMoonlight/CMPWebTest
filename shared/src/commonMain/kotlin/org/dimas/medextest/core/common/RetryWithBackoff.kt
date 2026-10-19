package org.dimas.medextest.core.common

import kotlinx.coroutines.delay
import kotlin.time.Duration

suspend fun <T> retryWithBackoff(
    attempts: Int,
    initialDelay: Duration,
    shouldRetry: (Throwable) -> Boolean = { it is NetworkException },
    onRetry: () -> Unit = {},
    block: suspend () -> T,
): T {
    require(attempts > 0) { "Attempts must be positive" }
    var pause = initialDelay
    repeat(attempts - 1) {
        val result = runCatchingCancellable { block() }
        val error = result.exceptionOrNull() ?: return result.getOrThrow()
        if (!shouldRetry(error)) throw error
        onRetry()
        delay(pause)
        pause *= 2
    }
    return block()
}
