package org.dimas.medextest.feature.home.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
internal data class QuickActionUi(
    val title: String,
    val icon: ImageVector,
    val tint: Color,
    val background: Color,
)
