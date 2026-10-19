package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.icon.MedExIcons
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
fun HeartBadge(
    filled: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
) {
    Box(
        modifier = modifier.size(size).clip(CircleShape).background(MedExColors.Chip),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (filled) MedExIcons.HeartFilled else MedExIcons.Heart,
            contentDescription = null,
            tint = if (filled) MedExColors.Pink else MedExColors.InkSecondary,
            modifier = Modifier.size(size * 0.55f),
        )
    }
}
