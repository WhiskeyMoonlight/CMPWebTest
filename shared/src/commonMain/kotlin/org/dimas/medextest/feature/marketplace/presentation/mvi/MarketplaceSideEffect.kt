package org.dimas.medextest.feature.marketplace.presentation.mvi

import org.dimas.medextest.core.common.UiMessage
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId

sealed interface MarketplaceSideEffect {
    data class OpenSection(val id: MarketplaceSectionId) : MarketplaceSideEffect
    data class ShowMessage(val message: UiMessage) : MarketplaceSideEffect
}
