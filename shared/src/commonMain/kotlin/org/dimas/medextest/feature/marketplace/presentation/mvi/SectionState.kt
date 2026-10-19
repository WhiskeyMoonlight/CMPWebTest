package org.dimas.medextest.feature.marketplace.presentation.mvi

import androidx.compose.runtime.Immutable
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionData
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId

@Immutable
data class SectionState(val id: MarketplaceSectionId, val load: LoadState<MarketplaceSectionData>)
