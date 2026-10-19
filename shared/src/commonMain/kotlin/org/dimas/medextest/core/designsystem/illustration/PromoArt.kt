package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun PromoArt(tone: ArtTone, modifier: Modifier = Modifier) {
    Spacer(
        modifier.drawWithCache {
            val big = Brush.radialGradient(
                listOf(tone.accent.copy(alpha = 0.75f), tone.accent.copy(alpha = 0f)),
                center = Offset(size.width * 0.7f, size.height * 0.75f),
                radius = size.minDimension * 0.6f,
            )
            val small = Brush.radialGradient(
                listOf(Color.White.copy(alpha = 0.9f), Color.White.copy(alpha = 0f)),
                center = Offset(size.width * 0.35f, size.height * 0.85f),
                radius = size.minDimension * 0.35f,
            )
            onDrawBehind {
                drawRect(tone.background)
                drawRect(big)
                drawRect(small)
            }
        },
    )
}
