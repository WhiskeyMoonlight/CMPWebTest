package org.dimas.medextest.compute.scan

object ScanPixels {
    private const val HASH_X = 374_761_393
    private const val HASH_Y = 668_265_263
    private const val HASH_SEED = 144_269
    private const val HASH_MIX = 1_274_126_177
    private const val NOISE_MASK = 0x3f
    private const val PAPER_MASK = 0xbf
    private const val BYTE_MASK = 0xff

    fun value(x: Int, y: Int, seed: Int): Int {
        var hash = x * HASH_X + y * HASH_Y + seed * HASH_SEED
        hash = (hash xor (hash ushr 13)) * HASH_MIX
        hash = hash xor (hash ushr 16)
        val noise = hash and NOISE_MASK
        val pattern = (x xor y) and PAPER_MASK
        return (noise + pattern) and BYTE_MASK
    }
}
