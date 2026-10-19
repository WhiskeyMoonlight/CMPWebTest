package org.dimas.medextest.feature.more.capabilities.presentation.mvi

sealed interface CapabilitiesSideEffect {
    data class ShowMessage(val text: String) : CapabilitiesSideEffect
}
