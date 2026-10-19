package org.dimas.medextest.core.background

import org.dimas.medextest.core.compute.ProgressSink
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
class TaskProgress : ProgressSink {
    private val total = AtomicInt(0)
    private val done = AtomicInt(0)

    val fraction: Float
        get() {
            val expected = total.load()
            return if (expected <= 0) 0f else (done.load().toFloat() / expected).coerceIn(0f, 1f)
        }

    val completedSteps: Int get() = done.load()

    fun expect(steps: Int) {
        total.addAndFetch(steps)
    }

    override fun advance(steps: Int) {
        done.addAndFetch(steps)
    }
}
