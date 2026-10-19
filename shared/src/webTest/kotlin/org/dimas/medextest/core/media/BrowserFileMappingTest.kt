package org.dimas.medextest.core.media

import kotlinx.coroutines.test.runTest
import org.dimas.medextest.core.compute.arrayBuffer
import org.dimas.medextest.testutil.textFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class BrowserFileMappingTest {
    @Test
    fun browser_file_is_mapped_with_metadata_and_content() = runTest {
        val picked = textFile("Привет, MedEx", "заметка.txt", "text/plain").toPickedFile()

        assertEquals("заметка.txt", picked.name)
        assertEquals("text/plain", picked.mimeType)
        assertEquals("Привет, MedEx".encodeToByteArray().size.toLong(), picked.sizeBytes)
        assertEquals("Привет, MedEx", picked.readBytes().decodeToString())
        assertIs<BrowserFileHandle>(picked.platformHandle)
    }

    @Test
    fun empty_mime_type_becomes_null() = runTest {
        assertNull(textFile("x", "blob", "").toPickedFile().mimeType)
    }

    @Test
    fun picked_file_exposes_array_buffer_for_worker_transfer() = runTest {
        val picked = textFile("12345", "digits.txt", "text/plain").toPickedFile()

        assertEquals(5, picked.arrayBuffer()?.byteLength)
    }

    @Test
    fun file_without_browser_handle_has_no_array_buffer() = runTest {
        val picked = PickedFile(name = "memory.bin", mimeType = null, sizeBytes = 1, reader = { ByteArray(1) })

        assertNull(picked.arrayBuffer())
    }
}
