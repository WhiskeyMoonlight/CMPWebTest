package org.dimas.medextest.feature.more.presentation.mvi

import org.dimas.medextest.core.common.UiMessage

sealed interface MoreSideEffect {
    data class ShowMessage(val message: UiMessage) : MoreSideEffect
}
