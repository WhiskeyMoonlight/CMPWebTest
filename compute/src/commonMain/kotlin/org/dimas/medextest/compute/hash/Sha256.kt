package org.dimas.medextest.compute.hash

class Sha256 {
    private val state = INITIAL_STATE.copyOf()
    private val block = ByteArray(BLOCK_SIZE)
    private val words = IntArray(ROUNDS)
    private var blockLength = 0
    private var totalBytes = 0L

    fun update(input: ByteArray, offset: Int = 0, length: Int = input.size - offset): Sha256 {
        require(offset >= 0 && length >= 0 && offset + length <= input.size) { "Range $offset+$length is outside ${input.size}" }
        var position = offset
        val end = offset + length
        if (blockLength > 0) {
            val copied = minOf(BLOCK_SIZE - blockLength, length)
            input.copyInto(block, blockLength, position, position + copied)
            blockLength += copied
            position += copied
            if (blockLength == BLOCK_SIZE) {
                compress(block, 0)
                blockLength = 0
            }
        }
        while (end - position >= BLOCK_SIZE) {
            compress(input, position)
            position += BLOCK_SIZE
        }
        if (position < end) {
            input.copyInto(block, 0, position, end)
            blockLength = end - position
        }
        totalBytes += length
        return this
    }

    fun digest(): ByteArray {
        val bitLength = totalBytes * Byte.SIZE_BITS
        block[blockLength++] = PADDING_MARKER
        if (blockLength > BLOCK_SIZE - LENGTH_SIZE) {
            block.fill(0, blockLength, BLOCK_SIZE)
            compress(block, 0)
            blockLength = 0
        }
        block.fill(0, blockLength, BLOCK_SIZE - LENGTH_SIZE)
        for (index in 0 until LENGTH_SIZE) {
            block[BLOCK_SIZE - 1 - index] = (bitLength ushr (index * Byte.SIZE_BITS)).toByte()
        }
        compress(block, 0)
        return ByteArray(DIGEST_SIZE) { index -> (state[index / Int.SIZE_BYTES] ushr (24 - (index % Int.SIZE_BYTES) * 8)).toByte() }
    }

    private fun compress(data: ByteArray, offset: Int) {
        for (t in 0 until 16) {
            val base = offset + t * Int.SIZE_BYTES
            words[t] = (data[base].toInt() and 0xff shl 24) or
                (data[base + 1].toInt() and 0xff shl 16) or
                (data[base + 2].toInt() and 0xff shl 8) or
                (data[base + 3].toInt() and 0xff)
        }
        for (t in 16 until ROUNDS) {
            val w15 = words[t - 15]
            val w2 = words[t - 2]
            val s0 = w15.rotateRight(7) xor w15.rotateRight(18) xor (w15 ushr 3)
            val s1 = w2.rotateRight(17) xor w2.rotateRight(19) xor (w2 ushr 10)
            words[t] = words[t - 16] + s0 + words[t - 7] + s1
        }
        var a = state[0]
        var b = state[1]
        var c = state[2]
        var d = state[3]
        var e = state[4]
        var f = state[5]
        var g = state[6]
        var h = state[7]
        for (t in 0 until ROUNDS) {
            val sigma1 = e.rotateRight(6) xor e.rotateRight(11) xor e.rotateRight(25)
            val choice = (e and f) xor (e.inv() and g)
            val temp1 = h + sigma1 + choice + ROUND_CONSTANTS[t] + words[t]
            val sigma0 = a.rotateRight(2) xor a.rotateRight(13) xor a.rotateRight(22)
            val majority = (a and b) xor (a and c) xor (b and c)
            val temp2 = sigma0 + majority
            h = g
            g = f
            f = e
            e = d + temp1
            d = c
            c = b
            b = a
            a = temp1 + temp2
        }
        state[0] += a
        state[1] += b
        state[2] += c
        state[3] += d
        state[4] += e
        state[5] += f
        state[6] += g
        state[7] += h
    }

    companion object {
        const val BLOCK_SIZE = 64
        const val DIGEST_SIZE = 32
        private const val ROUNDS = 64
        private const val LENGTH_SIZE = 8
        private const val PADDING_MARKER: Byte = 0x80.toByte()

        private val INITIAL_STATE: IntArray = intArrayOfUnsigned(
            0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19,
        )

        private val ROUND_CONSTANTS: IntArray = intArrayOfUnsigned(
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2,
        )

        private fun intArrayOfUnsigned(vararg values: Long): IntArray = IntArray(values.size) { values[it].toInt() }
    }
}
