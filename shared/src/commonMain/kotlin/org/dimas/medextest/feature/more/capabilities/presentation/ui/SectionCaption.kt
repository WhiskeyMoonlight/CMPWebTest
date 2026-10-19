package org.dimas.medextest.feature.more.capabilities.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
internal fun SectionCaption(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MedExColors.InkSecondary,
        modifier = modifier.padding(start = 32.dp, top = 20.dp, bottom = 8.dp),
    )
}
