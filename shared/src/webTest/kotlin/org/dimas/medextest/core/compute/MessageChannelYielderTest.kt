package org.dimas.medextest.core.compute

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageChannelYielderTest {
    private val yielder = MessageChannelYielder()

    @Test
    fun yield_resumes_after_macrotask() = runTest {
        val order = mutableListOf<String>()

        withContext(Dispatchers.Default) {
            coroutineScope {
                launch {
                    order += "before"
                    yielder.yieldToUi()
                    order += "after"
                }
                yield()
                order += "other"
            }
        }

        assertEquals(listOf("before", "other", "after"), order)
    }

    @Test
    fun many_concurrent_yields_all_resume() = runTest {
        val results = withContext(Dispatchers.Default) {
            (1..50).map { index -> async { repeat(3) { yielder.yieldToUi() }; index } }.awaitAll()
        }

        assertEquals((1..50).toList(), results)
    }

    @Test
    fun cancelled_yield_does_not_break_following_yields() = runTest {
        withContext(Dispatchers.Default) {
            val cancelled = launch { yielder.yieldToUi() }
            cancelled.cancel()
            repeat(5) { yielder.yieldToUi() }
        }
    }
}
