package org.dimas.medextest.feature.more.devsettings.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.dimas.medextest.core.designsystem.theme.MedExColors

internal const val PERF_HUD_SWITCH_TAG = "devsettings.perfHud"

@Composable
internal fun PerfHudSettingsCard(
    enabled: Boolean,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsCard(title = "Производительность", modifier = modifier) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("HUD поверх экранов", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Кадры в секунду, худший кадр и задержка UI-потока за последние 250 мс",
                    style = MaterialTheme.typography.bodySmall,
                    color = MedExColors.InkSecondary,
                )
            }
            Switch(checked = enabled, onCheckedChange = onChange, modifier = Modifier.testTag(PERF_HUD_SWITCH_TAG))
        }
    }
}
