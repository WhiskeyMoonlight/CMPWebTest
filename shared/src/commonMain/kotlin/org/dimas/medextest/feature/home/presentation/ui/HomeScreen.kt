package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.WindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.contentMaxWidth
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.core.designsystem.theme.MedExTheme
import org.dimas.medextest.feature.checkin.domain.model.Mood
import org.dimas.medextest.feature.home.domain.model.DayPart
import org.dimas.medextest.feature.home.domain.model.Greeting
import org.dimas.medextest.feature.home.domain.model.HomeDashboard
import org.dimas.medextest.feature.home.domain.model.QuickAction
import org.dimas.medextest.feature.home.domain.model.WaterIntake
import org.dimas.medextest.feature.home.presentation.mvi.HomeActions
import org.dimas.medextest.feature.home.presentation.mvi.HomeState

@Composable
internal fun HomeScreen(
    state: HomeState,
    actions: HomeActions,
    snackbarHostState: SnackbarHostState,
    onSearchClick: () -> Unit,
    onCallClinic: (phone: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val widthClass = LocalWindowWidthClass.current
    Box(modifier = modifier.fillMaxSize().background(MedExColors.Background)) {
        if (widthClass == WindowWidthClass.Expanded) {
            HomeTwoColumnContent(
                state = state,
                actions = actions,
                onSearchClick = onSearchClick,
                onCallClinic = onCallClinic,
                contentMaxWidth = widthClass.contentMaxWidth,
            )
        } else {
            HomeSingleColumnContent(
                state = state,
                actions = actions,
                onSearchClick = onSearchClick,
                onCallClinic = onCallClinic,
                contentMaxWidth = widthClass.contentMaxWidth,
            )
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

private object PreviewHomeActions : HomeActions {
    override fun onRetry() = Unit
    override fun onQuickActionClick(action: QuickAction) = Unit
    override fun onFillHealthProfileClick() = Unit
    override fun onCalendarClick() = Unit
    override fun onMoodClick(mood: Mood) = Unit
    override fun onAddWaterClick() = Unit
    override fun onCheckInClosed(submitted: Boolean) = Unit
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MedExTheme {
        HomeScreen(
            state = HomeState(
                greeting = Greeting(DayPart.Morning, firstName = "Анна", initials = "АМ", avatarSeed = 0),
                dashboard = LoadState.Content(HomeDashboard(QuickAction.entries, "+74950000000", showHealthProfilePromo = true)),
                water = LoadState.Content(WaterIntake(consumedMl = 250, goalMl = 1500, glassMl = 250)),
            ),
            actions = PreviewHomeActions,
            snackbarHostState = SnackbarHostState(),
            onSearchClick = {},
            onCallClinic = {},
        )
    }
}
