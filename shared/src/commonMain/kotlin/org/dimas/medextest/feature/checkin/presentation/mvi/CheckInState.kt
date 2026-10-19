package org.dimas.medextest.feature.checkin.presentation.mvi

import androidx.compose.runtime.Immutable
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.feature.checkin.domain.model.Mood
import org.dimas.medextest.feature.checkin.domain.model.Symptom

@Immutable
data class CheckInState(
    val mood: Mood? = null,
    val wellbeing: Int? = null,
    val symptoms: LoadState<List<Symptom>> = LoadState.Loading,
    val selectedSymptomIds: Set<String> = emptySet(),
    val hasNote: Boolean = false,
    val isSubmitting: Boolean = false,
    val submitFailed: Boolean = false,
) {
    val canSubmit: Boolean get() = mood != null || wellbeing != null || selectedSymptomIds.isNotEmpty() || hasNote
}
