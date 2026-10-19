package org.dimas.medextest.feature.more.devsettings.presentation.mvi

import androidx.compose.runtime.Immutable
import org.dimas.medextest.core.mock.MockBackendConfig

@Immutable
data class DevSettingsState(
    val config: MockBackendConfig = MockBackendConfig(),
    val perfHudEnabled: Boolean = false,
)
