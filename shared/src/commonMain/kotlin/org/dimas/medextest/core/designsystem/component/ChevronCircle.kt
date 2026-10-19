package org.dimas.medextest.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.icon.MedExIcons
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
fun ChevronCircle(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentDescription: String? = null,
) {
    Box(
        modifier = modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(MedExColors.Chip)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = MedExIcons.ChevronRight,
            contentDescription = contentDescription,
            tint = MedExColors.InkSecondary,
            modifier = Modifier.size(16.dp),
        )
    }
}
