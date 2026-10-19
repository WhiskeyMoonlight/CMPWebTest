package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

@Composable
internal fun <T : Any> TileCarousel(
    items: List<T>,
    key: (T) -> Any,
    tile: @Composable (T) -> Unit,
) {
    LazyRow(
        contentPadding = MarketplaceTileDefaults.RowPadding,
        horizontalArrangement = MarketplaceTileDefaults.RowSpacing,
    ) {
        items(items, key = key) { tile(it) }
    }
}
