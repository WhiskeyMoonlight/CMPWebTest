package org.dimas.medextest.feature.marketplace.domain.model

data class SearchResult(val id: String, val title: String, val section: MarketplaceSectionId, val priceFrom: Int?)
