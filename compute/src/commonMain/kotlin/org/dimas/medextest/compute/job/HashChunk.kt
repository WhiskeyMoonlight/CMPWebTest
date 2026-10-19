package org.dimas.medextest.compute.job

object HashChunk {
    const val SIZE_BYTES = 64 * 1024

    fun stepsFor(sizeBytes: Int): Int = (sizeBytes + SIZE_BYTES - 1) / SIZE_BYTES
}
