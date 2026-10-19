package org.dimas.medextest.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.app.navigation.AppNavigator
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.WindowWidthClass
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.core.designsystem.theme.MedExTheme
import org.dimas.medextest.core.platform.BindPlatformToUi
import org.dimas.medextest.feature.onboarding.domain.repository.OnboardingRepository
import org.dimas.medextest.feature.onboarding.presentation.ui.OnboardingRoute
import org.koin.compose.koinInject

@Composable
fun MedExApp(
    navigatorBinding: @Composable (AppNavigator) -> Unit = {},
    measurePerformance: Boolean = true,
) {
    MedExTheme {
        BindPlatformToUi()
        val onboarding = koinInject<OnboardingRepository>()
        val isOnboardingCompleted by onboarding.isCompleted.collectAsState()
        BoxWithConstraints(Modifier.fillMaxSize().background(MedExColors.Background)) {
            CompositionLocalProvider(LocalWindowWidthClass provides WindowWidthClass.fromWidth(maxWidth)) {
                if (isOnboardingCompleted) MainScaffold(navigatorBinding) else OnboardingRoute()
            }
            PerformanceOverlay(
                measure = measurePerformance,
                modifier = Modifier.align(Alignment.TopEnd).statusBarsPadding().padding(8.dp),
            )
        }
    }
}
