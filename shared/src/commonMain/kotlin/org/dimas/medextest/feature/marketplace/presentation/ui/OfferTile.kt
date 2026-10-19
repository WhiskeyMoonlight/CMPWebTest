package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.core.format.formatPriceFrom
import org.dimas.medextest.feature.marketplace.domain.model.ServiceOffer

@Composable
internal fun OfferTile(offer: ServiceOffer, onClick: () -> Unit) {
    OutlinedTile(width = MarketplaceTileDefaults.Width, onClick = onClick) {
        Column(Modifier.height(104.dp).padding(12.dp)) {
            Text(
                text = offer.title,
                style = MaterialTheme.typography.titleSmall,
                color = MedExColors.Ink,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.weight(1f))
            Text(formatPriceFrom(offer.priceFrom), style = MaterialTheme.typography.bodySmall, color = MedExColors.InkSecondary)
        }
    }
}
