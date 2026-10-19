package org.dimas.medextest.feature.checkin.presentation.mvi

import androidx.compose.runtime.Stable
import org.dimas.medextest.feature.checkin.domain.model.Mood

@Stable
interface CheckInActions {
    fun onOpened(preselectedMood: Mood?)

    fun onMoodSelect(mood: Mood)

    fun onWellbeingSelect(value: Int)

    fun onSymptomToggle(id: String)

    fun onRetrySymptoms()

    fun onPrimaryClick()
}
