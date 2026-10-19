package org.dimas.medextest.feature.more.devsettings.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.contentMaxWidth
import org.dimas.medextest.core.designsystem.component.BackTopBar
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.more.devsettings.presentation.mvi.DevSettingsActions
import org.dimas.medextest.feature.more.devsettings.presentation.mvi.DevSettingsState

@Composable
fun DevSettingsScreen(
    state: DevSettingsState,
    actions: DevSettingsActions,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    loadLab: @Composable () -> Unit = {},
) {
    val contentMaxWidth = LocalWindowWidthClass.current.contentMaxWidth
    Box(modifier.fillMaxSize().background(MedExColors.Background)) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(Modifier.widthIn(max = contentMaxWidth).fillMaxWidth()) {
                BackTopBar(title = "Для разработчика", onBack = onBack)
                Text(
                    text = "Поведение мок-бэкенда применяется к следующим запросам. Вернитесь на экран или нажмите «Повторить», чтобы увидеть эффект.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MedExColors.InkSecondary,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                LatencySettingsCard(selected = state.config.latency, onSelect = actions::onLatencySelect)
                FailureModeSettingsCard(selected = state.config.failureMode, onSelect = actions::onFailureModeSelect)
                ForceEmptySettingsCard(enabled = state.config.forceEmpty, onChange = actions::onForceEmptyChange)
                PerfHudSettingsCard(enabled = state.perfHudEnabled, onChange = actions::onPerfHudChange)
                loadLab()
                ResetSettingsCard(
                    onResetOnboarding = actions::onResetOnboardingClick,
                    onResetCheckIn = actions::onResetCheckInClick,
                )
                Spacer(Modifier.height(24.dp))
            }
        }
        SnackbarHost(snackbarHostState, Modifier.align(Alignment.BottomCenter))
    }
}
