package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.runtime.Composable
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionData
import org.dimas.medextest.feature.marketplace.presentation.mvi.MarketplaceActions

@Composable
internal fun SectionContent(data: MarketplaceSectionData, actions: MarketplaceActions) {
    when (data) {
        is MarketplaceSectionData.Promotions -> TileCarousel(data.items, key = { it.id }) {
            PromoTile(it, onClick = actions::onItemClick)
        }
        is MarketplaceSectionData.Doctors -> TileCarousel(data.items, key = { it.id }) {
            DoctorTile(it, onClick = actions::onItemClick)
        }
        is MarketplaceSectionData.LabTests -> TileCarousel(data.items, key = { it.id }) {
            OfferTile(it, onClick = actions::onItemClick)
        }
        is MarketplaceSectionData.Packages -> TileCarousel(data.items, key = { it.id }) {
            PackageTile(it, onClick = actions::onItemClick)
        }
        is MarketplaceSectionData.Examinations -> TileCarousel(data.items, key = { it.id }) {
            OfferTile(it, onClick = actions::onItemClick)
        }
        is MarketplaceSectionData.Services -> ServicesList(data.items, onItemClick = actions::onItemClick)
    }
}
