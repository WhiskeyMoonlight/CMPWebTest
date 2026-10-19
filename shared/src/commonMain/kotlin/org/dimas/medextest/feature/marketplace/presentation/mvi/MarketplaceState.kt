package org.dimas.medextest.feature.marketplace.presentation.mvi

import androidx.compose.runtime.Immutable
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionData
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId

@Immutable
data class MarketplaceState(
    val sections: List<SectionState> = MarketplaceSectionId.entries.map { SectionState(it, LoadState.Loading) },
    val search: SearchState = SearchState.Idle,
) {
    fun withSection(id: MarketplaceSectionId, load: LoadState<MarketplaceSectionData>): MarketplaceState =
        copy(sections = sections.map { section -> if (section.id == id) section.copy(load = load) else section })
}
