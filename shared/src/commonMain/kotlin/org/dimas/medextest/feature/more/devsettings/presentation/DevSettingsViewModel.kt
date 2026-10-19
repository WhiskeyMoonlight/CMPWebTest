package org.dimas.medextest.feature.more.devsettings.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.combine
import org.dimas.medextest.core.mock.FailureMode
import org.dimas.medextest.core.perf.PerfHudPreference
import org.dimas.medextest.feature.checkin.domain.repository.CheckInRepository
import org.dimas.medextest.feature.more.devsettings.data.DevSettingsRepository
import org.dimas.medextest.feature.more.devsettings.presentation.mvi.DevSettingsActions
import org.dimas.medextest.feature.more.devsettings.presentation.mvi.DevSettingsSideEffect
import org.dimas.medextest.feature.more.devsettings.presentation.mvi.DevSettingsState
import org.dimas.medextest.feature.onboarding.domain.repository.OnboardingRepository
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer
import kotlin.time.Duration

class DevSettingsViewModel(
    private val settings: DevSettingsRepository,
    private val perfHud: PerfHudPreference,
    private val onboarding: OnboardingRepository,
    private val checkIn: CheckInRepository,
) : ViewModel(), OrbitContainerHost<DevSettingsState, DevSettingsState, DevSettingsSideEffect>, DevSettingsActions {

    override val container: OrbitContainer<DevSettingsState, DevSettingsState, DevSettingsSideEffect> =
        orbitContainer(initialState = DevSettingsState(settings.config.value, perfHud.isEnabled.value)) {
            repeatOnSubscription {
                combine(settings.config, perfHud.isEnabled, ::DevSettingsState)
                    .collect { updated -> reduce { updated } }
            }
        }

    override fun onLatencySelect(latency: Duration) = settings.setLatency(latency)

    override fun onFailureModeSelect(mode: FailureMode) = settings.setFailureMode(mode)

    override fun onForceEmptyChange(enabled: Boolean) = settings.setForceEmpty(enabled)

    override fun onPerfHudChange(enabled: Boolean) = perfHud.setEnabled(enabled)

    override fun onResetOnboardingClick() = onboarding.reset()

    override fun onResetCheckInClick() {
        checkIn.reset()
        intent { postSideEffect(DevSettingsSideEffect.ShowMessage("Чек-ин появится при следующем открытии Главной")) }
    }
}
