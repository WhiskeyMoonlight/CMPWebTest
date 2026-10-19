package org.dimas.medextest.feature.more.loadlab.presentation.mvi

sealed interface LoadLabSideEffect {
    data class ShowMessage(val text: String) : LoadLabSideEffect
}
