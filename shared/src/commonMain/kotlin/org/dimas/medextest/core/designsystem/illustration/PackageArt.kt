package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.sin

private const val HELIX_STEPS = 40

@Composable
fun PackageArt(tone: ArtTone, modifier: Modifier = Modifier) {
    Spacer(
        modifier.drawWithCache {
            val background = Brush.linearGradient(listOf(tone.background, Color.White))
            val strandA = helixPath(size.width, size.height, phase = 0.0)
            val strandB = helixPath(size.width, size.height, phase = PI)
            val stroke = Stroke(width = size.minDimension * 0.035f)
            onDrawBehind {
                drawRect(background)
                drawPath(strandA, tone.accent, style = stroke)
                drawPath(strandB, tone.accent.copy(alpha = 0.55f), style = stroke)
            }
        },
    )
}

private fun helixPath(width: Float, height: Float, phase: Double): Path = Path().apply {
    repeat(HELIX_STEPS + 1) { i ->
        val t = i / HELIX_STEPS.toFloat()
        val x = width * (0.1f + 0.8f * t)
        val y = height * (0.5f - 0.25f * sin(t * 3 * PI + phase).toFloat()) - height * 0.15f * (t - 0.5f)
        if (i == 0) moveTo(x, y) else lineTo(x, y)
    }
}
