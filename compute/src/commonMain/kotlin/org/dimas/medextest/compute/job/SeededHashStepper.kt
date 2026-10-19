package org.dimas.medextest.compute.job

import org.dimas.medextest.compute.data.SeededByteStream
import org.dimas.medextest.compute.hash.Sha256
import org.dimas.medextest.compute.hash.toHex

internal class SeededHashStepper(private val job: HashJob) : Stepper {
    private val source = SeededByteStream(job.seed)
    private val sha256 = Sha256()
    private val chunk = ByteArray(minOf(HashChunk.SIZE_BYTES, job.sizeBytes))
    private var processed = 0

    override val isDone: Boolean get() = processed >= job.sizeBytes

    override fun step() {
        check(!isDone) { "Stepper is already done" }
        val length = minOf(chunk.size, job.sizeBytes - processed)
        source.fill(chunk, 0, length)
        sha256.update(chunk, 0, length)
        processed += length
    }

    override fun outcome(): ComputeOutcome {
        check(isDone) { "Stepper is not done yet" }
        return ComputeOutcome.Hash(sizeBytes = job.sizeBytes.toLong(), sha256 = sha256.digest().toHex())
    }
}
