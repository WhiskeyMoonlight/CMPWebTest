package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.feature.home.domain.model.WaterIntake
import org.dimas.medextest.feature.home.presentation.mvi.HomeActions

@Composable
internal fun HomeActivities(
    water: LoadState<WaterIntake>,
    isAddingWater: Boolean,
    actions: HomeActions,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        ActivitiesTitle(Modifier.padding(top = 14.dp, bottom = 12.dp))
        MoodCard(onMoodClick = actions::onMoodClick)
        WaterCard(
            water = water,
            isAdding = isAddingWater,
            onAddClick = actions::onAddWaterClick,
            onRetry = actions::onRetry,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
