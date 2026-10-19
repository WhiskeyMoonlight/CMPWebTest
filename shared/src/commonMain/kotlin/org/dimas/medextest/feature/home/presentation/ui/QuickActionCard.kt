package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.SurfaceCard
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.home.domain.model.QuickAction
import org.dimas.medextest.feature.home.presentation.util.ui

@Composable
internal fun QuickActionCard(
    action: QuickAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val ui = action.ui()
    SurfaceCard(modifier = modifier.height(118.dp), onClick = onClick) {
        Column(Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(ui.background),
                contentAlignment = Alignment.Center,
            ) {
                Icon(ui.icon, contentDescription = null, tint = ui.tint, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.weight(1f))
            Text(text = ui.title, style = MaterialTheme.typography.bodyMedium, color = MedExColors.Ink, maxLines = 2)
        }
    }
}
