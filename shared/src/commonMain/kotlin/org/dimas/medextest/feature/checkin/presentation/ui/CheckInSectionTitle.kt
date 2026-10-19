package org.dimas.medextest.feature.checkin.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
internal fun CheckInSectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MedExColors.Ink,
        modifier = modifier.padding(bottom = 12.dp),
    )
}
