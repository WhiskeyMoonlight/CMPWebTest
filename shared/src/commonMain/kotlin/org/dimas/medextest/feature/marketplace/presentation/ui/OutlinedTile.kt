package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
internal fun OutlinedTile(
    width: Dp,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .width(width)
            .clip(MarketplaceTileDefaults.Shape)
            .border(1.dp, MedExColors.Outline, MarketplaceTileDefaults.Shape)
            .clickable(onClick = onClick),
        content = content,
    )
}
