package org.dimas.medextest.compute.job

interface Stepper {
    val isDone: Boolean

    fun step()

    fun outcome(): ComputeOutcome
}
