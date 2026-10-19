package org.dimas.medextest.feature.checkin

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.dimas.medextest.feature.checkin.domain.model.CheckInEntry
import org.dimas.medextest.feature.checkin.domain.model.Mood
import org.dimas.medextest.feature.checkin.domain.usecase.ShouldShowDailyCheckInUseCase
import org.dimas.medextest.feature.checkin.domain.usecase.SubmitCheckInUseCase
import org.dimas.medextest.testutil.FakeCheckInRepository
import org.dimas.medextest.testutil.fixedTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private val TODAY = LocalDate(2026, 10, 14)

class DailyCheckInTest {
    private val time = fixedTime("2026-10-14T09:00:00Z")

    @Test
    fun shown_when_never_prompted() {
        assertTrue(ShouldShowDailyCheckInUseCase(FakeCheckInRepository(), time)())
    }

    @Test
    fun not_shown_twice_on_the_same_day() {
        val repository = FakeCheckInRepository().apply { lastPrompted = TODAY }
        assertFalse(ShouldShowDailyCheckInUseCase(repository, time)())
    }

    @Test
    fun shown_again_the_next_day() {
        val repository = FakeCheckInRepository().apply { lastPrompted = LocalDate(2026, 10, 13) }
        assertTrue(ShouldShowDailyCheckInUseCase(repository, time)())
    }

    @Test
    fun not_shown_after_submitting_today() {
        val repository = FakeCheckInRepository().apply { lastSubmitted = TODAY }
        assertFalse(ShouldShowDailyCheckInUseCase(repository, time)())
    }

    @Test
    fun empty_entry_is_rejected_and_note_is_trimmed() = runTest {
        val repository = FakeCheckInRepository()
        val submit = SubmitCheckInUseCase(repository, time)

        assertFailsWith<IllegalArgumentException> { submit(CheckInEntry(null, null, emptySet(), "   ")) }
        submit(CheckInEntry(Mood.Good, wellbeing = 4, symptomIds = emptySet(), note = "  спал хорошо  "))

        assertEquals("спал хорошо", repository.submitted.single().note)
        assertEquals(TODAY, repository.lastSubmitted)
    }
}
