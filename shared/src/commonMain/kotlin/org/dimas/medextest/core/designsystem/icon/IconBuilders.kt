package org.dimas.medextest.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

private const val ICON_SIZE = 24f
private const val STROKE_WIDTH = 1.8f

internal fun lineIcon(name: String, vararg paths: String): ImageVector =
    iconBuilder(name).apply {
        paths.forEach { data ->
            addPath(
                pathData = addPathNodes(data),
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = STROKE_WIDTH,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            )
        }
    }.build()

internal fun filledIcon(name: String, vararg paths: String): ImageVector =
    iconBuilder(name).apply {
        paths.forEach { data -> addPath(pathData = addPathNodes(data), fill = SolidColor(Color.Black)) }
    }.build()

private fun iconBuilder(name: String) = ImageVector.Builder(
    name = name,
    defaultWidth = ICON_SIZE.dp,
    defaultHeight = ICON_SIZE.dp,
    viewportWidth = ICON_SIZE,
    viewportHeight = ICON_SIZE,
)
