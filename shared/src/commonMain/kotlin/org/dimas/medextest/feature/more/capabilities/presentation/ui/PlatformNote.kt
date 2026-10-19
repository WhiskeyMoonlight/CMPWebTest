package org.dimas.medextest.feature.more.capabilities.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.core.platform.PlatformInfo
import org.dimas.medextest.core.platform.PlatformKind

@Composable
internal fun PlatformNote(
    platform: PlatformInfo,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = platform.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MedExColors.InkSecondary,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        if (platform.kind == PlatformKind.Web) {
            Text(
                text = "В браузере камера и геолокация работают только по HTTPS или на localhost",
                style = MaterialTheme.typography.bodySmall,
                color = MedExColors.InkTertiary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            )
        }
    }
}
