package org.dimas.medextest.core.common

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.milliseconds

class RetryWithBackoffTest {
    @Test
    fun returns_first_success_without_waiting() = runTest {
        var calls = 0

        val result = retryWithBackoff(attempts = 3, initialDelay = 100.milliseconds) { calls++; "ok" }

        assertEquals("ok", result)
        assertEquals(1, calls)
        assertEquals(0, currentTime)
    }

    @Test
    fun retries_network_errors_with_doubling_delay() = runTest {
        val attemptsAt = mutableListOf<Long>()
        var retries = 0

        val result = retryWithBackoff(attempts = 3, initialDelay = 100.milliseconds, onRetry = { retries++ }) {
            attemptsAt += currentTime
            if (attemptsAt.size < 3) throw NetworkException("GET /x") else "ok"
        }

        assertEquals("ok", result)
        assertEquals(listOf(0L, 100L, 300L), attemptsAt)
        assertEquals(2, retries)
    }

    @Test
    fun rethrows_last_network_error_after_all_attempts() = runTest {
        var calls = 0

        assertFailsWith<NetworkException> {
            retryWithBackoff(attempts = 3, initialDelay = 10.milliseconds) { calls++; throw NetworkException("GET /x") }
        }
        assertEquals(3, calls)
    }

    @Test
    fun does_not_retry_other_errors() = runTest {
        var calls = 0

        assertFailsWith<IllegalStateException> {
            retryWithBackoff(attempts = 3, initialDelay = 10.milliseconds) { calls++; error("bug") }
        }
        assertEquals(1, calls)
    }

    @Test
    fun cancellation_is_not_retried() = runTest {
        var calls = 0

        assertFailsWith<CancellationException> {
            retryWithBackoff(attempts = 3, initialDelay = 10.milliseconds) { calls++; throw CancellationException("stop") }
        }
        assertEquals(1, calls)
    }
}
