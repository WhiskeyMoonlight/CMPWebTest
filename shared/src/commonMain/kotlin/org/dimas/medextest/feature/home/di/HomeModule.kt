package org.dimas.medextest.feature.home.di

import org.dimas.medextest.feature.home.data.MockHomeRepository
import org.dimas.medextest.feature.home.domain.repository.HomeRepository
import org.dimas.medextest.feature.home.domain.usecase.AddWaterGlassUseCase
import org.dimas.medextest.feature.home.domain.usecase.GetGreetingUseCase
import org.dimas.medextest.feature.home.domain.usecase.GetHomeDashboardUseCase
import org.dimas.medextest.feature.home.domain.usecase.GetWaterIntakeUseCase
import org.dimas.medextest.feature.home.presentation.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    single<HomeRepository> { MockHomeRepository(get()) }
    factoryOf(::GetGreetingUseCase)
    factoryOf(::GetHomeDashboardUseCase)
    factoryOf(::GetWaterIntakeUseCase)
    factoryOf(::AddWaterGlassUseCase)
    viewModelOf(::HomeViewModel)
}
