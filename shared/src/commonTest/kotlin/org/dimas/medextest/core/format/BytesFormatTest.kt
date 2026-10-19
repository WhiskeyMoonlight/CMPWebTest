package org.dimas.medextest.core.format

import kotlin.test.Test
import kotlin.test.assertEquals

class BytesFormatTest {
    @Test
    fun bytes_are_humanized() {
        assertEquals("512${NBSP}Б", formatBytes(512))
        assertEquals("2${NBSP}КБ", formatBytes(2048))
        assertEquals("1,5${NBSP}МБ", formatBytes(1_572_864))
    }
}
