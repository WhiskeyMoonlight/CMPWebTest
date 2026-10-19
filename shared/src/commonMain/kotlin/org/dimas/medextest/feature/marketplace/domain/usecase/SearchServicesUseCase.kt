package org.dimas.medextest.feature.marketplace.domain.usecase

import org.dimas.medextest.feature.marketplace.domain.model.SearchResult
import org.dimas.medextest.feature.marketplace.domain.repository.MarketplaceRepository

class SearchServicesUseCase(private val repository: MarketplaceRepository) {
    suspend operator fun invoke(query: String): List<SearchResult> {
        val normalized = query.trim()
        if (normalized.length < MIN_QUERY_LENGTH) return emptyList()
        return repository.search(normalized)
    }

    companion object {
        const val MIN_QUERY_LENGTH = 2
    }
}
