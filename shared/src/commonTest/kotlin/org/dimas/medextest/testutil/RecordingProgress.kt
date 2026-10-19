package org.dimas.medextest.testutil

import org.dimas.medextest.core.compute.ProgressSink

class RecordingProgress(private val onAdvance: (Int) -> Unit = {}) : ProgressSink {
    var steps = 0
        private set

    override fun advance(steps: Int) {
        this.steps += steps
        onAdvance(this.steps)
    }
}
