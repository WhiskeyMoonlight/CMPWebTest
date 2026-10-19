package org.dimas.medextest.feature.marketplace.presentation.mvi

import androidx.compose.runtime.Stable
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId
import org.dimas.medextest.feature.marketplace.domain.model.SearchResult

@Stable
interface MarketplaceActions {
    fun onRetrySection(id: MarketplaceSectionId)

    fun onSectionMoreClick(id: MarketplaceSectionId)

    fun onItemClick()

    fun onSearchSubmit()

    fun onSearchResultClick(result: SearchResult)
}
