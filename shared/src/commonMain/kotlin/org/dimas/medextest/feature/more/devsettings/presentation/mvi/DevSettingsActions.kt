package org.dimas.medextest.feature.more.devsettings.presentation.mvi

import androidx.compose.runtime.Stable
import org.dimas.medextest.core.mock.FailureMode
import kotlin.time.Duration

@Stable
interface DevSettingsActions {
    fun onLatencySelect(latency: Duration)

    fun onFailureModeSelect(mode: FailureMode)

    fun onForceEmptyChange(enabled: Boolean)

    fun onPerfHudChange(enabled: Boolean)

    fun onResetOnboardingClick()

    fun onResetCheckInClick()
}
