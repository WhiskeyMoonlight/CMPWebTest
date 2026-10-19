package org.dimas.medextest.core.compute

fun interface ProgressSink {
    fun advance(steps: Int)
}
