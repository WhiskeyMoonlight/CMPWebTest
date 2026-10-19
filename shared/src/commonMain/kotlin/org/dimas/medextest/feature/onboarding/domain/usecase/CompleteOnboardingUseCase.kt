package org.dimas.medextest.feature.onboarding.domain.usecase

import org.dimas.medextest.feature.onboarding.domain.repository.OnboardingRepository

class CompleteOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke() = repository.complete()
}
