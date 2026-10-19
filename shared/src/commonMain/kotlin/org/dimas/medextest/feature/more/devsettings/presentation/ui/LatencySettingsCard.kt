package org.dimas.medextest.feature.more.devsettings.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.mock.MockBackendConfig
import org.dimas.medextest.feature.more.devsettings.presentation.util.label
import kotlin.time.Duration

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun LatencySettingsCard(
    selected: Duration,
    onSelect: (Duration) -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsCard(title = "Задержка ответа", modifier = modifier) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MockBackendConfig.LATENCY_OPTIONS.forEach { latency ->
                SettingChoice(latency.label(), selected = selected == latency, onClick = { onSelect(latency) })
            }
        }
    }
}
