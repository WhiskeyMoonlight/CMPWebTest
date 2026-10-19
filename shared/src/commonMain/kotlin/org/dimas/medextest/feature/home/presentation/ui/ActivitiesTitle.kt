package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
internal fun ActivitiesTitle(modifier: Modifier = Modifier) {
    Text(
        text = "Активности на сегодня",
        style = MaterialTheme.typography.titleLarge,
        color = MedExColors.Ink,
        modifier = modifier,
    )
}
