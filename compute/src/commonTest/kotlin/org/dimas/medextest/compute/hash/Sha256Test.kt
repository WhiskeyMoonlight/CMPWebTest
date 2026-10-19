package org.dimas.medextest.compute.hash

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class Sha256Test {
    private fun sha256(text: String): String = Sha256().update(text.encodeToByteArray()).digest().toHex()

    @Test
    fun empty_input_matches_reference_digest() {
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", sha256(""))
    }

    @Test
    fun abc_matches_reference_digest() {
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", sha256("abc"))
    }

    @Test
    fun two_block_message_matches_reference_digest() {
        assertEquals(
            "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
            sha256("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"),
        )
    }

    @Test
    fun million_a_matches_reference_digest() {
        val chunk = ByteArray(1000) { 'a'.code.toByte() }
        val hasher = Sha256()
        repeat(1000) { hasher.update(chunk) }
        assertEquals("cdc76e5c9914fb9281a1c7e284d73e67f1809a48a497200e046d39ccc7112cd0", hasher.digest().toHex())
    }

    @Test
    fun incremental_updates_with_random_chunks_match_single_update() {
        val random = Random(7)
        val data = ByteArray(10_000) { random.nextInt().toByte() }
        val expected = Sha256().update(data).digest().toHex()

        val hasher = Sha256()
        var position = 0
        while (position < data.size) {
            val length = minOf(random.nextInt(1, 200), data.size - position)
            hasher.update(data, position, length)
            position += length
        }

        assertEquals(expected, hasher.digest().toHex())
    }

    @Test
    fun messages_around_padding_boundary_hash_differently() {
        val digests = (54..66).map { length -> Sha256().update(ByteArray(length)).digest().toHex() }

        assertEquals(digests.size, digests.toSet().size)
    }
}
