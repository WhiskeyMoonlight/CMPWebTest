package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.core.designsystem.component.ErrorBlock
import org.dimas.medextest.core.designsystem.component.SkeletonBox
import org.dimas.medextest.core.designsystem.component.SurfaceCard
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.home.domain.model.WaterIntake

@Composable
internal fun WaterCard(
    water: LoadState<WaterIntake>,
    isAdding: Boolean,
    onAddClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(text = "Вода", style = MaterialTheme.typography.titleMedium, color = MedExColors.Ink)
            when (water) {
                LoadState.Loading -> {
                    Spacer(Modifier.height(8.dp))
                    SkeletonBox(Modifier.fillMaxWidth().height(72.dp))
                }
                LoadState.Empty -> Text(
                    text = "Нет данных за сегодня",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MedExColors.InkSecondary,
                )
                is LoadState.Error -> ErrorBlock(water.error, onRetry = onRetry)
                is LoadState.Content -> WaterGlasses(water = water.data, isAdding = isAdding, onAddClick = onAddClick)
            }
        }
    }
}
