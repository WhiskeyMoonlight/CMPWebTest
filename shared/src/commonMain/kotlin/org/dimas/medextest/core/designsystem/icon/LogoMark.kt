package org.dimas.medextest.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

val LogoMark: ImageVector by lazy {
    val viewport = LogoMarkPaths.VIEWPORT
    ImageVector.Builder("logo", viewport.dp, viewport.dp, viewport, viewport).apply {
        LogoMarkPaths.Connectors.forEach { addPath(addPathNodes(it), fill = SolidColor(Color(0xFF3AB2C2))) }
        addPath(addPathNodes(LogoMarkPaths.CENTER_RING), fill = SolidColor(Color(0xFFFEFEFE)))
        LogoMarkPaths.Dots.forEach { addPath(addPathNodes(it), fill = SolidColor(Color(0xFF00849E))) }
    }.build()
}
