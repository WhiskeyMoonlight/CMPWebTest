package org.dimas.medextest.feature.onboarding.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface OnboardingRepository {
    val isCompleted: StateFlow<Boolean>

    fun complete()

    fun reset()
}
