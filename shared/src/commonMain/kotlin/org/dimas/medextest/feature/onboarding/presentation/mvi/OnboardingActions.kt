package org.dimas.medextest.feature.onboarding.presentation.mvi

import androidx.compose.runtime.Stable

@Stable
interface OnboardingActions {
    fun onNextClick()

    fun onHaveAccountClick()
}
