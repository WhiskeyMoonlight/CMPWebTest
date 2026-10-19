package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.ui.graphics.Color

enum class MoodFace(internal val base: Color, internal val highlight: Color, internal val smile: Float) {
    Star(Color(0xFFF2589A), Color(0xFFFFB8D3), smile = -1f),
    Flower(Color(0xFFF5A05A), Color(0xFFFFD6AE), smile = -0.4f),
    Sun(Color(0xFFF5CF3B), Color(0xFFFFF3B0), smile = 0f),
    Square(Color(0xFF47C26A), Color(0xFFBDF1CA), smile = 0.5f),
    Clover(Color(0xFF4DB3EF), Color(0xFFBEE7FF), smile = 1f),
}
