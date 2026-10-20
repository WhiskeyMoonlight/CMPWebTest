package org.dimas.medextest.feature.checkin.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.core.common.runCatchingCancellable
import org.dimas.medextest.core.common.toLoadState
import org.dimas.medextest.feature.checkin.domain.model.CheckInEntry
import org.dimas.medextest.feature.checkin.domain.model.Mood
import org.dimas.medextest.feature.checkin.domain.usecase.GetSymptomsUseCase
import org.dimas.medextest.feature.checkin.domain.usecase.SubmitCheckInUseCase
import org.dimas.medextest.feature.checkin.presentation.mvi.CheckInActions
import org.dimas.medextest.feature.checkin.presentation.mvi.CheckInSideEffect
import org.dimas.medextest.feature.checkin.presentation.mvi.CheckInState
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.orbitContainer

class CheckInViewModel(
    private val getSymptoms: GetSymptomsUseCase,
    private val submitCheckIn: SubmitCheckInUseCase,
) : ViewModel(),
    OrbitContainerHost<CheckInState, CheckInState, CheckInSideEffect>,
    CheckInActions {

    val note = TextFieldState()

    override val container: OrbitContainer<CheckInState, CheckInState, CheckInSideEffect> =
        orbitContainer(
            initialState = CheckInState(),
        ) {
            coroutineScope {
                launch { loadSymptoms() }
                launch {
                    repeatOnSubscription {
                        snapshotFlow { note.text.isNotBlank() }
                            .distinctUntilChanged()
                            .collect { hasNote -> reduce { state.copy(hasNote = hasNote) } }
                    }
                }
            }
        }

    fun warmUp() = intent {}

    override fun onOpened(preselectedMood: Mood?) {
        intent {
            reduce { CheckInState(mood = preselectedMood, symptoms = state.symptoms) }
        }
    }

    override fun onMoodSelect(mood: Mood) {
        intent { reduce { state.copy(mood = if (state.mood == mood) null else mood) } }
    }

    override fun onWellbeingSelect(value: Int) {
        intent { reduce { state.copy(wellbeing = if (state.wellbeing == value) null else value) } }
    }

    override fun onSymptomToggle(id: String) {
        intent {
            reduce {
                val selected = state.selectedSymptomIds
                state.copy(selectedSymptomIds = if (id in selected) selected - id else selected + id)
            }
        }
    }

    override fun onRetrySymptoms() {
        intent { loadSymptoms() }
    }

    override fun onPrimaryClick() {
        intent {
            if (state.isSubmitting) return@intent
            if (!state.canSubmit) {
                postSideEffect(CheckInSideEffect.Close(submitted = false))
                return@intent
            }
            reduce { state.copy(isSubmitting = true, submitFailed = false) }
            val entry = CheckInEntry(
                mood = state.mood,
                wellbeing = state.wellbeing,
                symptomIds = state.selectedSymptomIds,
                note = note.text.toString(),
            )
            val result = runCatchingCancellable { submitCheckIn(entry) }
            reduce { state.copy(isSubmitting = false, submitFailed = result.isFailure) }
            if (result.isSuccess) postSideEffect(CheckInSideEffect.Close(submitted = true))
        }
    }

    private suspend fun Syntax<CheckInState, CheckInSideEffect>.loadSymptoms() {
        reduce { state.copy(symptoms = LoadState.Loading) }
        val result = runCatchingCancellable { getSymptoms() }
        reduce { state.copy(symptoms = result.toLoadState { it.isEmpty() }) }
    }
}
