package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private data class Blob(val centerX: Float, val centerY: Float, val radiusOfWidth: Float, val color: Color)

private val OnboardingBlobs = listOf(
    Blob(0.1f, 0.5f, 0.75f, Color(0xFFF7D3EE)),
    Blob(0.45f, 0.68f, 0.8f, Color(0xFFDCD3FA)),
    Blob(1.0f, 0.42f, 0.55f, Color(0xFFFBE6D6)),
    Blob(0.95f, 0.82f, 0.75f, Color(0xFFC3F7FA)),
)

fun Modifier.onboardingBackground(): Modifier = drawWithCache {
    val blobs = OnboardingBlobs.map { blob ->
        Brush.radialGradient(
            colors = listOf(blob.color, blob.color.copy(alpha = 0f)),
            center = Offset(size.width * blob.centerX, size.height * blob.centerY),
            radius = size.width * blob.radiusOfWidth,
        )
    }
    val topFade = Brush.verticalGradient(
        0f to Color.White,
        0.22f to Color.White,
        0.42f to Color.White.copy(alpha = 0f),
    )
    val bottomFade = Brush.verticalGradient(
        0.86f to Color.White.copy(alpha = 0f),
        1f to Color.White,
    )
    onDrawBehind {
        drawRect(Color.White)
        blobs.forEach { drawRect(it) }
        drawRect(topFade)
        drawRect(bottomFade)
    }
}
