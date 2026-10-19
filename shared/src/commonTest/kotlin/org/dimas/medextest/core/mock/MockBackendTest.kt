package org.dimas.medextest.core.mock

import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.dimas.medextest.core.common.NetworkException
import org.dimas.medextest.testutil.instantBackend
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.milliseconds

class MockBackendTest {
    @Test
    fun waits_for_the_configured_latency() = runTest {
        val backend = instantBackend(latency = 500.milliseconds)
        val started = currentTime

        val result = backend.respond("GET /x", empty = 0) { 42 }

        assertEquals(42, result)
        assertEquals(500, currentTime - started)
    }

    @Test
    fun always_failing_backend_throws_network_exception() = runTest {
        val backend = instantBackend(failureMode = FailureMode.Always)
        assertFailsWith<NetworkException> { backend.respond("GET /x", empty = 0) { 42 } }
    }

    @Test
    fun force_empty_returns_the_empty_payload() = runTest {
        val backend = instantBackend(forceEmpty = true)
        assertEquals(emptyList(), backend.respond("GET /x", empty = emptyList<Int>()) { listOf(1, 2) })
    }

    @Test
    fun call_ignores_force_empty() = runTest {
        val backend = instantBackend(forceEmpty = true)
        assertEquals(listOf(1, 2), backend.call("POST /x") { listOf(1, 2) })
    }
}
