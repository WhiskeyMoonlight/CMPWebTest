package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.illustration.headerGradientBackground
import org.dimas.medextest.feature.home.presentation.mvi.HomeActions
import org.dimas.medextest.feature.home.presentation.mvi.HomeState

private val ColumnSpacing = 24.dp

@Composable
internal fun HomeTwoColumnContent(
    state: HomeState,
    actions: HomeActions,
    onSearchClick: () -> Unit,
    onCallClinic: (phone: String) -> Unit,
    contentMaxWidth: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().headerGradientBackground(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Row(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(ColumnSpacing),
            ) {
                HomeHeader(
                    greeting = state.greeting,
                    dashboard = state.dashboard,
                    actions = actions,
                    onSearchClick = onSearchClick,
                    onCallClinic = onCallClinic,
                    modifier = Modifier.weight(1f),
                )
                HomeActivities(
                    water = state.water,
                    isAddingWater = state.isAddingWater,
                    actions = actions,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
