package org.dimas.medextest.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

private const val CROPPED_VIEWPORT = 64f
private const val CROP_OFFSET = -16f

val LogoMarkMono: ImageVector by lazy {
    ImageVector.Builder("logo-mono", CROPPED_VIEWPORT.dp, CROPPED_VIEWPORT.dp, CROPPED_VIEWPORT, CROPPED_VIEWPORT).apply {
        addGroup(translationX = CROP_OFFSET, translationY = CROP_OFFSET)
        LogoMarkPaths.Connectors.forEach { addPath(addPathNodes(it), fill = SolidColor(Color.White), fillAlpha = 0.65f) }
        LogoMarkPaths.Dots.forEach { addPath(addPathNodes(it), fill = SolidColor(Color.White)) }
        clearGroup()
    }.build()
}
