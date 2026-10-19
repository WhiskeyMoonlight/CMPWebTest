package org.dimas.medextest.core.format

import kotlin.test.Test
import kotlin.test.assertEquals

class RublesFormatTest {
    @Test
    fun rubles_are_grouped_only_from_five_digits() {
        assertEquals("1450$NBSP₽", formatRubles(1450))
        assertEquals("10${NBSP}500$NBSP₽", formatRubles(10_500))
    }

    @Test
    fun price_from_is_prefixed() {
        assertEquals("от${NBSP}1450$NBSP₽", formatPriceFrom(1450))
    }
}
