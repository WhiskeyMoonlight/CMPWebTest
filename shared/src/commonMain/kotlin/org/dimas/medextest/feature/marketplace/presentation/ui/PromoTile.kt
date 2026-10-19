package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.illustration.PromoArt
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.marketplace.domain.model.Promotion
import org.dimas.medextest.feature.marketplace.presentation.util.tone

private val BadgeShape = RoundedCornerShape(10.dp)

@Composable
internal fun PromoTile(promotion: Promotion, onClick: () -> Unit) {
    Box(
        Modifier
            .width(MarketplaceTileDefaults.Width)
            .height(MarketplaceTileDefaults.PromoHeight)
            .clip(MarketplaceTileDefaults.Shape)
            .clickable(onClick = onClick),
    ) {
        PromoArt(tone = promotion.palette.tone(), modifier = Modifier.fillMaxSize())
        Column(Modifier.padding(12.dp)) {
            Text(promotion.title, style = MaterialTheme.typography.titleSmall, color = MedExColors.Ink, maxLines = 3)
            Spacer(Modifier.height(6.dp))
            Text(
                text = promotion.badge,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                modifier = Modifier.clip(BadgeShape).background(MedExColors.Pink).padding(horizontal = 8.dp, vertical = 3.dp),
            )
        }
    }
}
