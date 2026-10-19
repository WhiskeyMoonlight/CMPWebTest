package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal fun starPath(size: Size, points: Int, innerRatio: Float): Path {
    val center = Offset(size.width / 2, size.height / 2)
    val outer = size.minDimension / 2
    val inner = outer * innerRatio
    return Path().apply {
        repeat(points * 2) { i ->
            val radius = if (i % 2 == 0) outer else inner
            val angle = i * PI / points - PI / 2
            val point = Offset(center.x + (radius * cos(angle)).toFloat(), center.y + (radius * sin(angle)).toFloat())
            if (i == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
        }
        close()
    }
}

internal fun petalsPath(size: Size, petals: Int): Path {
    val center = Offset(size.width / 2, size.height / 2)
    val petalRadius = size.minDimension * (if (petals == 4) 0.27f else 0.22f)
    val orbit = size.minDimension / 2 - petalRadius
    return Path().apply {
        addOval(Rect(center, size.minDimension * 0.3f))
        repeat(petals) { i ->
            val angle = i * 2 * PI / petals - PI / 2
            val petalCenter = Offset(center.x + (orbit * cos(angle)).toFloat(), center.y + (orbit * sin(angle)).toFloat())
            addOval(Rect(petalCenter, petalRadius))
        }
    }
}
