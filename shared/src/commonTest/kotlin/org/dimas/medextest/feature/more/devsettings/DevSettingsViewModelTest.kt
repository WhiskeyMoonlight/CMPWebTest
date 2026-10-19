package org.dimas.medextest.feature.more.devsettings

import kotlinx.coroutines.test.runTest
import org.dimas.medextest.core.perf.PerfHudPreference
import org.dimas.medextest.core.storage.InMemoryKeyValueStorage
import org.dimas.medextest.core.storage.StorageKeys
import org.dimas.medextest.feature.more.devsettings.data.DevSettingsRepository
import org.dimas.medextest.feature.more.devsettings.presentation.DevSettingsViewModel
import org.dimas.medextest.feature.onboarding.data.StorageOnboardingRepository
import org.dimas.medextest.testutil.FakeCheckInRepository
import org.dimas.medextest.testutil.MainDispatcherTest
import org.dimas.medextest.testutil.awaitStateMatching
import org.orbitmvi.orbit.test.testWithInternalState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DevSettingsViewModelTest : MainDispatcherTest() {
    @Test
    fun perf_hud_switch_is_persisted_and_reflected_in_state() = runTest {
        val storage = InMemoryKeyValueStorage()
        val hud = PerfHudPreference(storage)
        val viewModel = DevSettingsViewModel(
            settings = DevSettingsRepository(storage),
            perfHud = hud,
            onboarding = StorageOnboardingRepository(storage),
            checkIn = FakeCheckInRepository(),
        )

        viewModel.testWithInternalState(this) {
            runOnCreate()
            containerHost.onPerfHudChange(true)
            awaitStateMatching { it.perfHudEnabled }
            cancelAndIgnoreRemainingItems()
        }

        assertTrue(hud.isEnabled.value)
        assertEquals("true", storage.getString(StorageKeys.DEV_PERF_HUD))
        assertTrue(PerfHudPreference(storage).isEnabled.value)
    }
}
