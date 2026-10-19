package org.dimas.medextest.feature.marketplace.domain.usecase

import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionData
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId
import org.dimas.medextest.feature.marketplace.domain.repository.MarketplaceRepository

class LoadMarketplaceSectionUseCase(private val repository: MarketplaceRepository) {
    suspend operator fun invoke(id: MarketplaceSectionId): MarketplaceSectionData = repository.loadSection(id)
}
