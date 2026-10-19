package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp

private val GlassOutline = Color(0xFFD8E3EC)
private val GlassTop = Color(0xFFFFFFFF)
private val GlassBottom = Color(0xFFF1F6FA)
private val WaterTop = Color(0xFFA9DCF8)
private val WaterBottom = Color(0xFF5AB0EC)
private val PlusColor = Color(0xFFA9AEBC)

@Composable
fun WaterGlass(
    filled: Boolean,
    showPlus: Boolean,
    modifier: Modifier = Modifier.size(width = 40.dp, height = 56.dp),
) {
    Canvas(modifier) {
        val inset = size.width * 0.09f
        val glass = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width - inset, size.height)
            lineTo(inset, size.height)
            close()
        }
        drawPath(glass, Brush.verticalGradient(listOf(GlassTop, GlassBottom)))
        if (filled) {
            clipPath(glass) {
                val waterTop = size.height * 0.18f
                drawRect(
                    brush = Brush.verticalGradient(listOf(WaterTop, WaterBottom), startY = waterTop, endY = size.height),
                    topLeft = Offset(0f, waterTop),
                )
            }
        }
        drawPath(glass, GlassOutline, style = Stroke(width = 1.5.dp.toPx()))
        if (showPlus) {
            val center = Offset(size.width / 2, size.height / 2)
            val arm = size.width * 0.14f
            val stroke = 1.6.dp.toPx()
            drawLine(PlusColor, Offset(center.x - arm, center.y), Offset(center.x + arm, center.y), stroke, StrokeCap.Round)
            drawLine(PlusColor, Offset(center.x, center.y - arm), Offset(center.x, center.y + arm), stroke, StrokeCap.Round)
        }
    }
}
