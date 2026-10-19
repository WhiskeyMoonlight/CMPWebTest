package org.dimas.medextest.core.common

import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RunCatchingCancellableTest {
    @Test
    fun cancellation_is_rethrown() {
        assertFailsWith<CancellationException> { runCatchingCancellable { throw CancellationException("cancelled") } }
    }

    @Test
    fun other_errors_become_failures() {
        assertTrue(runCatchingCancellable { error("boom") }.isFailure)
    }
}
