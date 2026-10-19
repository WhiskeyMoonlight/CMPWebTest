package org.dimas.medextest.feature.marketplace.domain.repository

import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionData
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId
import org.dimas.medextest.feature.marketplace.domain.model.SearchResult

interface MarketplaceRepository {
    suspend fun loadSection(id: MarketplaceSectionId): MarketplaceSectionData

    suspend fun search(query: String): List<SearchResult>
}
