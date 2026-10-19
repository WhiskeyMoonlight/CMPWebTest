package org.dimas.medextest.feature.checkin

import kotlinx.coroutines.test.runTest
import org.dimas.medextest.feature.checkin.domain.model.Mood
import org.dimas.medextest.feature.checkin.domain.usecase.GetSymptomsUseCase
import org.dimas.medextest.feature.checkin.domain.usecase.SubmitCheckInUseCase
import org.dimas.medextest.feature.checkin.presentation.CheckInViewModel
import org.dimas.medextest.feature.checkin.presentation.mvi.CheckInSideEffect
import org.dimas.medextest.testutil.FakeCheckInRepository
import org.dimas.medextest.testutil.MainDispatcherTest
import org.dimas.medextest.testutil.fixedTime
import org.orbitmvi.orbit.test.testWithInternalState
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckInViewModelTest : MainDispatcherTest() {
    private val repository = FakeCheckInRepository()

    private fun viewModel() = CheckInViewModel(
        getSymptoms = GetSymptomsUseCase(repository),
        submitCheckIn = SubmitCheckInUseCase(repository, fixedTime("2026-10-14T09:00:00Z")),
    )

    @Test
    fun empty_form_closes_without_submitting() = runTest {
        viewModel().testWithInternalState(this) {
            containerHost.onPrimaryClick()
            expectSideEffect(CheckInSideEffect.Close(submitted = false))
        }
        assertEquals(0, repository.submitted.size)
    }

    @Test
    fun symptom_toggle_selects_and_deselects() = runTest {
        viewModel().testWithInternalState(this) {
            containerHost.onSymptomToggle("fine")
            expectInternalState { copy(selectedSymptomIds = setOf("fine")) }
            containerHost.onSymptomToggle("fine")
            expectInternalState { copy(selectedSymptomIds = emptySet()) }
        }
    }

    @Test
    fun selected_mood_is_submitted_and_closes_the_sheet() = runTest {
        viewModel().testWithInternalState(this) {
            containerHost.onMoodSelect(Mood.Good)
            expectInternalState { copy(mood = Mood.Good) }
            containerHost.onPrimaryClick()
            expectInternalState { copy(isSubmitting = true) }
            expectInternalState { copy(isSubmitting = false) }
            expectSideEffect(CheckInSideEffect.Close(submitted = true))
        }
        assertEquals(Mood.Good, repository.submitted.single().mood)
    }
}
