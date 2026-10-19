package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.illustration.PackageArt
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.core.format.formatRubles
import org.dimas.medextest.feature.marketplace.domain.model.ServicePackage
import org.dimas.medextest.feature.marketplace.presentation.util.tone

private val PackageTileWidth = 170.dp

@Composable
internal fun PackageTile(servicePackage: ServicePackage, onClick: () -> Unit) {
    OutlinedTile(width = PackageTileWidth, onClick = onClick) {
        PackageArt(tone = servicePackage.palette.tone(), modifier = Modifier.fillMaxWidth().height(104.dp))
        Column(Modifier.padding(10.dp)) {
            Text(
                text = servicePackage.title,
                style = MaterialTheme.typography.titleSmall,
                color = MedExColors.Ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            PackagePrice(price = servicePackage.price, oldPrice = servicePackage.oldPrice)
        }
    }
}

@Composable
private fun PackagePrice(price: Int, oldPrice: Int?) {
    Row {
        Text(
            text = formatRubles(price),
            style = MaterialTheme.typography.bodyMedium,
            color = if (oldPrice != null) MedExColors.Pink else MedExColors.Ink,
        )
        if (oldPrice != null) {
            Spacer(Modifier.width(6.dp))
            Text(
                text = formatRubles(oldPrice),
                style = MaterialTheme.typography.bodyMedium,
                color = MedExColors.InkTertiary,
                textDecoration = TextDecoration.LineThrough,
            )
        }
    }
}
