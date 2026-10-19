package org.dimas.medextest.compute.job

fun ComputeJob.stepper(): Stepper = when (this) {
    is HashJob -> SeededHashStepper(this)
    is LabParseJob -> LabParseStepper(this)
    is ScanBandJob -> ScanBandStepper(this)
}

fun bytesHashStepper(bytes: ByteArray): Stepper = BytesHashStepper(bytes)

fun Stepper.runToEnd(): ComputeOutcome {
    while (!isDone) step()
    return outcome()
}
