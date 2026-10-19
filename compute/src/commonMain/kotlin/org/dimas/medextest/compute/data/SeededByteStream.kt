package org.dimas.medextest.compute.data

class SeededByteStream(seed: Int) {
    private var state: Int = (seed xor SEED_MIX).takeIf { it != 0 } ?: SEED_MIX
    private var buffered = 0
    private var bufferedBytes = 0

    fun fill(target: ByteArray, offset: Int = 0, length: Int = target.size - offset) {
        for (index in offset until offset + length) {
            if (bufferedBytes == 0) {
                buffered = nextInt()
                bufferedBytes = Int.SIZE_BYTES
            }
            target[index] = buffered.toByte()
            buffered = buffered ushr Byte.SIZE_BITS
            bufferedBytes--
        }
    }

    private fun nextInt(): Int {
        var x = state
        x = x xor (x shl 13)
        x = x xor (x ushr 17)
        x = x xor (x shl 5)
        state = x
        return x
    }

    private companion object {
        const val SEED_MIX = 0x2545F491
    }
}
