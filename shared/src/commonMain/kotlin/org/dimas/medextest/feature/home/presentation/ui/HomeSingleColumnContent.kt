package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.illustration.headerGradientBackground
import org.dimas.medextest.feature.home.presentation.mvi.HomeActions
import org.dimas.medextest.feature.home.presentation.mvi.HomeState

@Composable
internal fun HomeSingleColumnContent(
    state: HomeState,
    actions: HomeActions,
    onSearchClick: () -> Unit,
    onCallClinic: (phone: String) -> Unit,
    contentMaxWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val itemModifier = Modifier.widthIn(max = contentMaxWidth).fillMaxWidth()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item(key = "top", contentType = "top") {
            Box(
                modifier = Modifier.fillMaxWidth().headerGradientBackground(),
                contentAlignment = Alignment.TopCenter,
            ) {
                HomeHeader(
                    greeting = state.greeting,
                    dashboard = state.dashboard,
                    actions = actions,
                    onSearchClick = onSearchClick,
                    onCallClinic = onCallClinic,
                    modifier = itemModifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp, bottom = 4.dp),
                )
            }
        }
        item(key = "activities-title", contentType = "title") {
            ActivitiesTitle(itemModifier.padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 12.dp))
        }
        item(key = "mood", contentType = "mood") {
            MoodCard(
                onMoodClick = actions::onMoodClick,
                modifier = itemModifier.padding(horizontal = 16.dp),
            )
        }
        item(key = "water", contentType = "water") {
            WaterCard(
                water = state.water,
                isAdding = state.isAddingWater,
                onAddClick = actions::onAddWaterClick,
                onRetry = actions::onRetry,
                modifier = itemModifier.padding(top = 8.dp).padding(horizontal = 16.dp),
            )
        }
    }
}
