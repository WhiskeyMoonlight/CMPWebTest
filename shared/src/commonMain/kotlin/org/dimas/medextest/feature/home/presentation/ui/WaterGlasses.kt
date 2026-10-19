package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.illustration.WaterGlass
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.core.format.formatLiters
import org.dimas.medextest.feature.home.domain.model.WaterIntake

@Composable
internal fun WaterGlasses(
    water: WaterIntake,
    isAdding: Boolean,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = "Выпито ${formatLiters(water.consumedMl)} из ${formatLiters(water.goalMl)} л",
            style = MaterialTheme.typography.bodyMedium,
            color = MedExColors.InkSecondary,
        )
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            repeat(water.glassCount) { index ->
                val isNext = index == water.filledGlasses && !water.isGoalReached
                WaterGlass(
                    filled = index < water.filledGlasses,
                    showPlus = isNext,
                    modifier = Modifier
                        .size(width = 40.dp, height = 56.dp)
                        .then(
                            if (isNext) {
                                Modifier.clickable(
                                    enabled = !isAdding,
                                    role = Role.Button,
                                    onClickLabel = "Добавить стакан воды",
                                    onClick = onAddClick,
                                )
                            } else {
                                Modifier
                            },
                        ),
                )
            }
        }
    }
}
