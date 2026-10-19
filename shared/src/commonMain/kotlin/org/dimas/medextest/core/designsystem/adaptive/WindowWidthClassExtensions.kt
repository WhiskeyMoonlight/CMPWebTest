package org.dimas.medextest.core.designsystem.adaptive

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val WindowWidthClass.isWide: Boolean
    get() = this != WindowWidthClass.Compact

val WindowWidthClass.contentMaxWidth: Dp
    get() = when (this) {
        WindowWidthClass.Compact -> Dp.Infinity
        WindowWidthClass.Medium -> 720.dp
        WindowWidthClass.Expanded -> 1080.dp
    }
