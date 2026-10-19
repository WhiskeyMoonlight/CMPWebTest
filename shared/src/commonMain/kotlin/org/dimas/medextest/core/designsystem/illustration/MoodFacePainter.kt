package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

private val FaceColor = Color(0xFF4A3530)

internal fun DrawScope.drawMoodFace(face: MoodFace) {
    val body = when (face) {
        MoodFace.Star -> starPath(size, points = 8, innerRatio = 0.68f)
        MoodFace.Flower -> petalsPath(size, petals = 6)
        MoodFace.Sun -> Path().apply { addOval(Rect(Offset.Zero, size)) }
        MoodFace.Square -> Path().apply { addRoundRect(RoundRect(Rect(Offset.Zero, size), CornerRadius(size.minDimension * 0.28f))) }
        MoodFace.Clover -> petalsPath(size, petals = 4)
    }
    drawPath(
        path = body,
        brush = Brush.radialGradient(
            colors = listOf(face.highlight, face.base),
            center = Offset(size.width * 0.35f, size.height * 0.3f),
            radius = size.minDimension * 0.9f,
        ),
    )
    drawFace(face.smile)
}

private fun DrawScope.drawFace(smile: Float) {
    val eyeRadius = size.minDimension * 0.045f
    val eyeY = size.height * 0.46f
    drawCircle(FaceColor, eyeRadius, Offset(size.width * 0.4f, eyeY))
    drawCircle(FaceColor, eyeRadius, Offset(size.width * 0.6f, eyeY))
    val mouthY = size.height * 0.6f
    val halfWidth = size.width * 0.08f
    val bend = size.height * 0.06f * smile
    val mouth = Path().apply {
        moveTo(size.width / 2 - halfWidth, mouthY)
        quadraticTo(size.width / 2, mouthY + bend, size.width / 2 + halfWidth, mouthY)
    }
    drawPath(mouth, FaceColor, style = Stroke(width = size.minDimension * 0.035f, cap = StrokeCap.Round))
}
