package org.dimas.medextest.feature.home

import org.dimas.medextest.feature.home.domain.model.DayPart
import kotlin.test.Test
import kotlin.test.assertEquals

class DayPartTest {
    @Test
    fun hours_map_to_day_parts_at_the_boundaries() {
        assertEquals(DayPart.Night, DayPart.of(4))
        assertEquals(DayPart.Morning, DayPart.of(5))
        assertEquals(DayPart.Morning, DayPart.of(11))
        assertEquals(DayPart.Day, DayPart.of(12))
        assertEquals(DayPart.Evening, DayPart.of(18))
        assertEquals(DayPart.Evening, DayPart.of(22))
        assertEquals(DayPart.Night, DayPart.of(23))
    }
}
