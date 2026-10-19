package org.dimas.medextest.feature.more.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.WindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.contentMaxWidth
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.more.presentation.mvi.MoreActions
import org.dimas.medextest.feature.more.presentation.mvi.MoreState

@Composable
fun MoreScreen(
    state: MoreState,
    actions: MoreActions,
    snackbarHostState: SnackbarHostState,
    onOpenCapabilities: () -> Unit,
    onOpenDevSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val widthClass = LocalWindowWidthClass.current
    Box(modifier.fillMaxSize().background(MedExColors.Background)) {
        if (widthClass == WindowWidthClass.Expanded) {
            MoreTwoColumnContent(
                profile = state.profile,
                onMenuItemClick = actions::onMenuItemClick,
                onOpenCapabilities = onOpenCapabilities,
                onOpenDevSettings = onOpenDevSettings,
                contentMaxWidth = widthClass.contentMaxWidth,
            )
        } else {
            MoreSingleColumnContent(
                profile = state.profile,
                onMenuItemClick = actions::onMenuItemClick,
                onOpenCapabilities = onOpenCapabilities,
                onOpenDevSettings = onOpenDevSettings,
                contentMaxWidth = widthClass.contentMaxWidth,
            )
        }
        SnackbarHost(snackbarHostState, Modifier.align(Alignment.BottomCenter))
    }
}
