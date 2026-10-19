package org.dimas.medextest.compute.data

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse

class SeededByteStreamTest {
    @Test
    fun same_seed_produces_same_bytes() {
        val first = ByteArray(1_000).also { SeededByteStream(42).fill(it) }
        val second = ByteArray(1_000).also { SeededByteStream(42).fill(it) }

        assertContentEquals(first, second)
    }

    @Test
    fun chunked_fill_matches_single_fill() {
        val whole = ByteArray(1_001).also { SeededByteStream(3).fill(it) }
        val chunked = ByteArray(1_001)
        val stream = SeededByteStream(3)
        stream.fill(chunked, 0, 5)
        stream.fill(chunked, 5, 333)
        stream.fill(chunked, 338, 663)

        assertContentEquals(whole, chunked)
    }

    @Test
    fun different_seeds_produce_different_bytes() {
        val first = ByteArray(64).also { SeededByteStream(1).fill(it) }
        val second = ByteArray(64).also { SeededByteStream(2).fill(it) }

        assertFalse(first.contentEquals(second))
    }
}
