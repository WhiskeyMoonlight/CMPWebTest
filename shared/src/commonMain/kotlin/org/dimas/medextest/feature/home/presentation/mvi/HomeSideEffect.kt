package org.dimas.medextest.feature.home.presentation.mvi

import org.dimas.medextest.core.common.UiMessage

sealed interface HomeSideEffect {
    data class ShowMessage(val message: UiMessage) : HomeSideEffect
}
