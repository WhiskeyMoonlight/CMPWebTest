package org.dimas.medextest.core.storage

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class KeyValueStorageTest {
    @Test
    fun local_date_round_trips_and_garbage_reads_as_null() {
        val storage = InMemoryKeyValueStorage()
        storage.putLocalDate("date", LocalDate(2026, 10, 14))
        storage.putString("broken", "not-a-date")

        assertEquals(LocalDate(2026, 10, 14), storage.getLocalDate("date"))
        assertNull(storage.getLocalDate("broken"))
    }

    @Test
    fun boolean_round_trips_and_missing_reads_as_false() {
        val storage = InMemoryKeyValueStorage()
        storage.putBoolean("flag", true)

        assertTrue(storage.getBoolean("flag"))
        assertFalse(storage.getBoolean("missing"))
    }
}
