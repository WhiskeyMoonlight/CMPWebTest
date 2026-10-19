package org.dimas.medextest.testutil

import org.dimas.medextest.core.common.NetworkException
import org.dimas.medextest.feature.marketplace.domain.model.DoctorSpecialty
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionData
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId
import org.dimas.medextest.feature.marketplace.domain.model.Palette
import org.dimas.medextest.feature.marketplace.domain.model.Promotion
import org.dimas.medextest.feature.marketplace.domain.model.SearchResult
import org.dimas.medextest.feature.marketplace.domain.model.ServiceCategory
import org.dimas.medextest.feature.marketplace.domain.model.ServiceOffer
import org.dimas.medextest.feature.marketplace.domain.model.ServicePackage
import org.dimas.medextest.feature.marketplace.domain.repository.MarketplaceRepository

class FakeMarketplaceRepository(private val failOnce: Set<MarketplaceSectionId> = emptySet()) : MarketplaceRepository {
    val calls = mutableMapOf<MarketplaceSectionId, Int>()

    override suspend fun loadSection(id: MarketplaceSectionId): MarketplaceSectionData {
        val call = (calls[id] ?: 0) + 1
        calls[id] = call
        if (id in failOnce && call == 1) throw NetworkException("GET /marketplace/${id.slug}")
        return when (id) {
            MarketplaceSectionId.Promotions -> MarketplaceSectionData.Promotions(listOf(Promotion("p", "Акция", "1000 ₽", Palette.Pink)))
            MarketplaceSectionId.Doctors -> MarketplaceSectionData.Doctors(listOf(DoctorSpecialty("d", "Невролог", 1450, listOf(1))))
            MarketplaceSectionId.LabTests -> MarketplaceSectionData.LabTests(listOf(ServiceOffer("l", "Гормоны", 1450)))
            MarketplaceSectionId.Packages -> MarketplaceSectionData.Packages(listOf(ServicePackage("k", "Пакет", 1450, null, Palette.Sky)))
            MarketplaceSectionId.Examinations -> MarketplaceSectionData.Examinations(listOf(ServiceOffer("e", "ЭКГ", 900)))
            MarketplaceSectionId.Services -> MarketplaceSectionData.Services(listOf(ServiceCategory("s", "Неврология")))
        }
    }

    override suspend fun search(query: String): List<SearchResult> = emptyList()
}
