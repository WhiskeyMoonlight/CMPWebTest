package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import org.dimas.medextest.core.designsystem.theme.MedExColors

fun Modifier.headerGradientBackground(): Modifier = drawWithCache {
    val horizontal = Brush.horizontalGradient(listOf(MedExColors.HeaderStart, MedExColors.HeaderEnd))
    val fade = Brush.verticalGradient(
        0.55f to MedExColors.Background.copy(alpha = 0f),
        1f to MedExColors.Background,
    )
    onDrawBehind {
        drawRect(horizontal)
        drawRect(fade)
    }
}
