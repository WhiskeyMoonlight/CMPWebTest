package org.dimas.medextest.feature.home.presentation.mvi

import androidx.compose.runtime.Stable
import org.dimas.medextest.feature.checkin.domain.model.Mood
import org.dimas.medextest.feature.home.domain.model.QuickAction

@Stable
interface HomeActions {
    fun onRetry()

    fun onQuickActionClick(action: QuickAction)

    fun onFillHealthProfileClick()

    fun onCalendarClick()

    fun onMoodClick(mood: Mood)

    fun onAddWaterClick()

    fun onCheckInClosed(submitted: Boolean)
}
