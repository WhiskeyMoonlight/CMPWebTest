package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal object MarketplaceTileDefaults {
    val Shape: Shape = RoundedCornerShape(16.dp)
    val Width: Dp = 160.dp
    val PromoHeight: Dp = 200.dp
    val RowPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
    val RowSpacing: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(8.dp)
}
