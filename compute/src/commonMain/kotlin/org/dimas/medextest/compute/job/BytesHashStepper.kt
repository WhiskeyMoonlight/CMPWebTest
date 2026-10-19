package org.dimas.medextest.compute.job

import org.dimas.medextest.compute.hash.Sha256
import org.dimas.medextest.compute.hash.toHex

internal class BytesHashStepper(private val bytes: ByteArray) : Stepper {
    private val sha256 = Sha256()
    private var processed = 0
    private var finished = bytes.isEmpty()

    override val isDone: Boolean get() = finished

    override fun step() {
        check(!isDone) { "Stepper is already done" }
        val length = minOf(HashChunk.SIZE_BYTES, bytes.size - processed)
        sha256.update(bytes, processed, length)
        processed += length
        finished = processed >= bytes.size
    }

    override fun outcome(): ComputeOutcome {
        check(isDone) { "Stepper is not done yet" }
        return ComputeOutcome.Hash(sizeBytes = bytes.size.toLong(), sha256 = sha256.digest().toHex())
    }
}
