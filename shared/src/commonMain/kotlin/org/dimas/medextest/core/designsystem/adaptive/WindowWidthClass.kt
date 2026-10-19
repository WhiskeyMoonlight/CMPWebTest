package org.dimas.medextest.core.designsystem.adaptive

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowWidthClass {
    Compact,
    Medium,
    Expanded;

    companion object {
        private val MediumMinWidth = 600.dp
        private val ExpandedMinWidth = 840.dp

        fun fromWidth(width: Dp): WindowWidthClass = when {
            width < MediumMinWidth -> Compact
            width < ExpandedMinWidth -> Medium
            else -> Expanded
        }
    }
}
