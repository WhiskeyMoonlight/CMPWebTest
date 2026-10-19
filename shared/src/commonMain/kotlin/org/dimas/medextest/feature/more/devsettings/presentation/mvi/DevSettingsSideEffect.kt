package org.dimas.medextest.feature.more.devsettings.presentation.mvi

sealed interface DevSettingsSideEffect {
    data class ShowMessage(val text: String) : DevSettingsSideEffect
}
