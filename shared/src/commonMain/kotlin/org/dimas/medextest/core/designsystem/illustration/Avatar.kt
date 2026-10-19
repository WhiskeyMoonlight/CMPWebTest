package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AvatarGradients = listOf(
    listOf(Color(0xFFF6C7B6), Color(0xFFD88D7A)),
    listOf(Color(0xFFB9D7F2), Color(0xFF6E9FD0)),
    listOf(Color(0xFFD6CBF5), Color(0xFF9483D6)),
    listOf(Color(0xFFC9EBD3), Color(0xFF69B884)),
    listOf(Color(0xFFF9DFA6), Color(0xFFD9A441)),
)

@Composable
fun Avatar(
    initials: String,
    seed: Int,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
) {
    val colors = AvatarGradients[seed.mod(AvatarGradients.size)]
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.linearGradient(colors)),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = initials, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = (size.value * 0.36f).sp)
    }
}
