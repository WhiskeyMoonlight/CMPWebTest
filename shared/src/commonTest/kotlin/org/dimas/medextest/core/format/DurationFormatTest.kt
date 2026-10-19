package org.dimas.medextest.core.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class DurationFormatTest {
    @Test
    fun short_durations_are_shown_in_milliseconds() {
        assertEquals("0${NBSP}мс", formatDuration(0.milliseconds))
        assertEquals("999${NBSP}мс", formatDuration(999.milliseconds))
    }

    @Test
    fun long_durations_are_shown_in_seconds_with_hundredths() {
        assertEquals("1,00${NBSP}с", formatDuration(1.seconds))
        assertEquals("12,34${NBSP}с", formatDuration(12_345.milliseconds))
        assertEquals("2,05${NBSP}с", formatDuration(2_050.milliseconds))
    }

    @Test
    fun throughput_is_megabytes_per_second() {
        assertEquals("64${NBSP}МБ/с", formatThroughput(64L * 1024 * 1024, 1.seconds))
        assertEquals("128${NBSP}МБ/с", formatThroughput(64L * 1024 * 1024, 500.milliseconds))
        assertEquals("—", formatThroughput(10, 0.milliseconds))
    }
}
