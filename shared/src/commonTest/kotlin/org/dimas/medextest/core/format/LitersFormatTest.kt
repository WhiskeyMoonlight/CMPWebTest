package org.dimas.medextest.core.format

import kotlin.test.Test
import kotlin.test.assertEquals

class LitersFormatTest {
    @Test
    fun liters_use_a_decimal_comma_without_trailing_zeros() {
        assertEquals("0,25", formatLiters(250))
        assertEquals("1,5", formatLiters(1500))
        assertEquals("1", formatLiters(1000))
    }
}
