package org.dimas.medextest.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.v2.runComposeUiTest
import org.dimas.medextest.core.designsystem.theme.MedExTheme
import org.dimas.medextest.core.mock.FailureMode
import org.dimas.medextest.feature.more.devsettings.presentation.mvi.DevSettingsActions
import org.dimas.medextest.feature.more.devsettings.presentation.mvi.DevSettingsState
import org.dimas.medextest.feature.more.devsettings.presentation.ui.DevSettingsScreen
import org.dimas.medextest.feature.more.devsettings.presentation.ui.PERF_HUD_SWITCH_TAG
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration

@OptIn(ExperimentalTestApi::class)
class DevSettingsScreenUiTest {
    private class RecordingActions : DevSettingsActions {
        val calls = mutableListOf<String>()

        override fun onLatencySelect(latency: Duration) {
            calls += "latency:$latency"
        }

        override fun onFailureModeSelect(mode: FailureMode) {
            calls += "failure:$mode"
        }

        override fun onForceEmptyChange(enabled: Boolean) {
            calls += "empty:$enabled"
        }

        override fun onPerfHudChange(enabled: Boolean) {
            calls += "hud:$enabled"
        }

        override fun onResetOnboardingClick() {
            calls += "resetOnboarding"
        }

        override fun onResetCheckInClick() {
            calls += "resetCheckIn"
        }
    }

    @Test
    fun hud_switch_and_load_section_are_part_of_dev_menu() = runComposeUiTest {
        val actions = RecordingActions()
        setContent {
            MedExTheme {
                DevSettingsScreen(
                    state = DevSettingsState(perfHudEnabled = false),
                    actions = actions,
                    snackbarHostState = SnackbarHostState(),
                    onBack = {},
                    loadLab = { Text("Секция нагрузки") },
                )
            }
        }

        onNodeWithTag(PERF_HUD_SWITCH_TAG).performScrollTo().assertIsOff().performClick()
        onNodeWithText("Секция нагрузки").performScrollTo().assertExists()

        assertEquals(listOf("hud:true"), actions.calls)
    }
}
