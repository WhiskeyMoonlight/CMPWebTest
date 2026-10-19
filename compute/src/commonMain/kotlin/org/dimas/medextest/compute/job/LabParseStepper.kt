package org.dimas.medextest.compute.job

import org.dimas.medextest.compute.lab.LabJson
import org.dimas.medextest.compute.lab.LabResult
import org.dimas.medextest.compute.lab.LabStats

internal class LabParseStepper(private val job: LabParseJob) : Stepper {
    private var parsed: List<LabResult>? = null
    private var stats: LabStats? = null

    override val isDone: Boolean get() = stats != null

    override fun step() {
        check(!isDone) { "Stepper is already done" }
        val records = parsed
        if (records == null) {
            parsed = LabJson.parsePage(job.json)
        } else {
            stats = LabStats.of(records)
            parsed = null
        }
    }

    override fun outcome(): ComputeOutcome = ComputeOutcome.Lab(checkNotNull(stats) { "Stepper is not done yet" })
}
