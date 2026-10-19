package org.dimas.medextest.feature.onboarding.di

import org.dimas.medextest.feature.onboarding.data.StorageOnboardingRepository
import org.dimas.medextest.feature.onboarding.domain.repository.OnboardingRepository
import org.dimas.medextest.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import org.dimas.medextest.feature.onboarding.presentation.OnboardingViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onboardingModule = module {
    single<OnboardingRepository> { StorageOnboardingRepository(get()) }
    factoryOf(::CompleteOnboardingUseCase)
    viewModelOf(::OnboardingViewModel)
}
