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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.icon.LogoMarkMono
import org.dimas.medextest.core.designsystem.theme.MedExColors

private const val ASSISTANT_LABEL = "AI-ассистент"
private val AssistantGradient = Brush.linearGradient(listOf(MedExColors.AssistantStart, MedExColors.AssistantEnd))

@Composable
fun AssistantButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(54.dp)
            .shadow(elevation = 8.dp, shape = CircleShape, spotColor = MedExColors.AssistantEnd)
            .clip(CircleShape)
            .background(AssistantGradient)
            .clickable(role = Role.Button, onClickLabel = ASSISTANT_LABEL, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(LogoMarkMono, contentDescription = ASSISTANT_LABEL, tint = Color.Unspecified, modifier = Modifier.size(34.dp))
    }
}
