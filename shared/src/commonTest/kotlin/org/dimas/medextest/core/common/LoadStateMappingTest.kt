package org.dimas.medextest.core.common

import kotlin.test.Test
import kotlin.test.assertEquals

class LoadStateMappingTest {
    @Test
    fun success_becomes_content_or_empty() {
        assertEquals(LoadState.Content(listOf(1)), Result.success(listOf(1)).toLoadState { it.isEmpty() })
        assertEquals(LoadState.Empty, Result.success(emptyList<Int>()).toLoadState { it.isEmpty() })
    }

    @Test
    fun network_failure_becomes_network_error() {
        val state = Result.failure<Int>(NetworkException("GET /x")).toLoadState()

        assertEquals(LoadState.Error(AppError.Network), state)
    }

    @Test
    fun other_failure_becomes_unexpected_error_with_message() {
        val state = Result.failure<Int>(IllegalStateException("boom")).toLoadState()

        assertEquals(LoadState.Error(AppError.Unexpected("boom")), state)
    }
}
