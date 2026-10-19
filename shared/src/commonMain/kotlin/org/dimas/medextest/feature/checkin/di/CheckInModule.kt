package org.dimas.medextest.feature.checkin.di

import org.dimas.medextest.feature.checkin.data.CheckInRepositoryImpl
import org.dimas.medextest.feature.checkin.domain.repository.CheckInRepository
import org.dimas.medextest.feature.checkin.domain.usecase.GetSymptomsUseCase
import org.dimas.medextest.feature.checkin.domain.usecase.MarkCheckInPromptedUseCase
import org.dimas.medextest.feature.checkin.domain.usecase.ShouldShowDailyCheckInUseCase
import org.dimas.medextest.feature.checkin.domain.usecase.SubmitCheckInUseCase
import org.dimas.medextest.feature.checkin.presentation.CheckInViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val checkInModule = module {
    single<CheckInRepository> { CheckInRepositoryImpl(get(), get()) }
    factoryOf(::ShouldShowDailyCheckInUseCase)
    factoryOf(::MarkCheckInPromptedUseCase)
    factoryOf(::GetSymptomsUseCase)
    factoryOf(::SubmitCheckInUseCase)
    viewModelOf(::CheckInViewModel)
}
