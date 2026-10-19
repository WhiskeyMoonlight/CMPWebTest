package org.dimas.medextest.feature.marketplace.data

import org.dimas.medextest.core.mock.MockBackend
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionData
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId
import org.dimas.medextest.feature.marketplace.domain.model.SearchResult
import org.dimas.medextest.feature.marketplace.domain.repository.MarketplaceRepository

class MockMarketplaceRepository(private val backend: MockBackend) : MarketplaceRepository {

    override suspend fun loadSection(id: MarketplaceSectionId): MarketplaceSectionData =
        backend.respond(endpoint = "GET /marketplace/${id.slug}", empty = emptySection(id)) { section(id) }

    override suspend fun search(query: String): List<SearchResult> =
        backend.respond(endpoint = "GET /marketplace/search", empty = emptyList()) {
            searchIndex.filter { it.title.contains(query, ignoreCase = true) }
        }

    private fun section(id: MarketplaceSectionId): MarketplaceSectionData = when (id) {
        MarketplaceSectionId.Promotions -> MarketplaceSectionData.Promotions(MarketplaceCatalog.promotions)
        MarketplaceSectionId.Doctors -> MarketplaceSectionData.Doctors(MarketplaceCatalog.doctors)
        MarketplaceSectionId.LabTests -> MarketplaceSectionData.LabTests(MarketplaceCatalog.labTests)
        MarketplaceSectionId.Packages -> MarketplaceSectionData.Packages(MarketplaceCatalog.packages)
        MarketplaceSectionId.Examinations -> MarketplaceSectionData.Examinations(MarketplaceCatalog.examinations)
        MarketplaceSectionId.Services -> MarketplaceSectionData.Services(MarketplaceCatalog.services)
    }

    private fun emptySection(id: MarketplaceSectionId): MarketplaceSectionData = when (id) {
        MarketplaceSectionId.Promotions -> MarketplaceSectionData.Promotions(emptyList())
        MarketplaceSectionId.Doctors -> MarketplaceSectionData.Doctors(emptyList())
        MarketplaceSectionId.LabTests -> MarketplaceSectionData.LabTests(emptyList())
        MarketplaceSectionId.Packages -> MarketplaceSectionData.Packages(emptyList())
        MarketplaceSectionId.Examinations -> MarketplaceSectionData.Examinations(emptyList())
        MarketplaceSectionId.Services -> MarketplaceSectionData.Services(emptyList())
    }

    private val searchIndex: List<SearchResult> by lazy {
        MarketplaceCatalog.doctors.map { SearchResult(it.id, it.title, MarketplaceSectionId.Doctors, it.priceFrom) } +
            MarketplaceCatalog.labTests.map { SearchResult(it.id, it.title, MarketplaceSectionId.LabTests, it.priceFrom) } +
            MarketplaceCatalog.packages.map { SearchResult(it.id, it.title, MarketplaceSectionId.Packages, it.price) } +
            MarketplaceCatalog.examinations.map { SearchResult(it.id, it.title, MarketplaceSectionId.Examinations, it.priceFrom) } +
            MarketplaceCatalog.services.map { SearchResult(it.id, it.title, MarketplaceSectionId.Services, null) }
    }
}
