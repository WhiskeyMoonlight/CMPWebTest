package org.dimas.medextest.core.background

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskProgressTest {
    @Test
    fun fraction_is_zero_until_work_is_expected() {
        val progress = TaskProgress()
        progress.advance(5)

        assertEquals(0f, progress.fraction)
    }

    @Test
    fun fraction_follows_completed_steps_and_is_clamped() {
        val progress = TaskProgress()
        progress.expect(4)
        progress.advance(1)
        assertEquals(0.25f, progress.fraction)

        progress.advance(10)
        assertEquals(1f, progress.fraction)
    }

    @Test
    fun expectations_accumulate() {
        val progress = TaskProgress()
        progress.expect(2)
        progress.expect(2)
        progress.advance(1)

        assertEquals(0.25f, progress.fraction)
    }

    @Test
    fun concurrent_advances_from_many_threads_are_not_lost() = runTest {
        val progress = TaskProgress()

        withContext(Dispatchers.Default) {
            repeat(WORKERS) { launch { repeat(STEPS_PER_WORKER) { progress.advance(1) } } }
        }

        assertEquals(WORKERS * STEPS_PER_WORKER, progress.completedSteps)
    }

    private companion object {
        const val WORKERS = 8
        const val STEPS_PER_WORKER = 20_000
    }
}
