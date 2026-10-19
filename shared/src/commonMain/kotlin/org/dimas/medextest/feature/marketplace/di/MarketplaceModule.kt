package org.dimas.medextest.feature.marketplace.di

import org.dimas.medextest.feature.marketplace.data.MockMarketplaceRepository
import org.dimas.medextest.feature.marketplace.domain.repository.MarketplaceRepository
import org.dimas.medextest.feature.marketplace.domain.usecase.LoadMarketplaceSectionUseCase
import org.dimas.medextest.feature.marketplace.domain.usecase.SearchServicesUseCase
import org.dimas.medextest.feature.marketplace.presentation.MarketplaceViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val marketplaceModule = module {
    single<MarketplaceRepository> { MockMarketplaceRepository(get()) }
    factoryOf(::LoadMarketplaceSectionUseCase)
    factoryOf(::SearchServicesUseCase)
    viewModelOf(::MarketplaceViewModel)
}
