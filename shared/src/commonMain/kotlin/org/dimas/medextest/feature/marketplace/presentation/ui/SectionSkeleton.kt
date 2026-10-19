package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.WindowWidthClass
import org.dimas.medextest.core.designsystem.component.SkeletonBox
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId

private const val SERVICES_SKELETON_ROWS = 5
private val TileSkeletonHeight = 120.dp

@Composable
internal fun SectionSkeleton(id: MarketplaceSectionId) {
    val widthClass = LocalWindowWidthClass.current
    if (id == MarketplaceSectionId.Services) {
        ServicesSkeleton(columns = if (widthClass == WindowWidthClass.Expanded) 2 else 1)
    } else {
        TilesSkeleton(
            count = widthClass.skeletonTileCount(),
            height = if (id == MarketplaceSectionId.Promotions) MarketplaceTileDefaults.PromoHeight else TileSkeletonHeight,
        )
    }
}

@Composable
private fun TilesSkeleton(count: Int, height: Dp) {
    Row(Modifier.padding(MarketplaceTileDefaults.RowPadding), horizontalArrangement = MarketplaceTileDefaults.RowSpacing) {
        repeat(count) {
            SkeletonBox(Modifier.size(width = MarketplaceTileDefaults.Width, height = height), shape = MarketplaceTileDefaults.Shape)
        }
    }
}

@Composable
private fun ServicesSkeleton(columns: Int) {
    Row(Modifier.padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(40.dp)) {
        repeat(columns) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                repeat(SERVICES_SKELETON_ROWS) { SkeletonBox(Modifier.fillMaxWidth().height(20.dp)) }
            }
        }
    }
}

private fun WindowWidthClass.skeletonTileCount(): Int = when (this) {
    WindowWidthClass.Compact -> 3
    WindowWidthClass.Medium -> 4
    WindowWidthClass.Expanded -> 6
}
