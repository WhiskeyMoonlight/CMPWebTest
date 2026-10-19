package org.dimas.medextest.feature.more.capabilities.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.PrimaryButton
import org.dimas.medextest.core.designsystem.component.SurfaceCard
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.more.capabilities.presentation.model.LocationUi
import org.dimas.medextest.feature.more.capabilities.presentation.util.summary

@Composable
internal fun LocationCard(
    location: LocationUi,
    onLocate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SurfaceCard(modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            LocationResult(location)
            Spacer(Modifier.height(12.dp))
            PrimaryButton(text = "Определить геопозицию", onClick = onLocate, loading = location == LocationUi.Requesting)
        }
    }
}

@Composable
private fun LocationResult(location: LocationUi) {
    val (text, color) = when (location) {
        LocationUi.Idle -> "Нажмите кнопку — система спросит разрешение" to MedExColors.InkSecondary
        LocationUi.Requesting -> "Определяем…" to MedExColors.InkSecondary
        is LocationUi.Failed -> location.reason to MedExColors.Red
        is LocationUi.Located -> location.summary() to MedExColors.Ink
    }
    Text(text, style = MaterialTheme.typography.bodyMedium, color = color)
}
