package org.dimas.medextest.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import org.dimas.medextest.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import org.dimas.medextest.feature.onboarding.presentation.mvi.OnboardingActions
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class OnboardingViewModel(
    private val completeOnboarding: CompleteOnboardingUseCase,
) : ViewModel(), OrbitContainerHost<Unit, Unit, Nothing>, OnboardingActions {

    override val container: OrbitContainer<Unit, Unit, Nothing> = orbitContainer(initialState = Unit)

    override fun onNextClick() {
        intent { completeOnboarding() }
    }

    override fun onHaveAccountClick() {
        intent { completeOnboarding() }
    }
}
