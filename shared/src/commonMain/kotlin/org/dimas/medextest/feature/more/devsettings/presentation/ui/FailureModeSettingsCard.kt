package org.dimas.medextest.feature.more.devsettings.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.mock.FailureMode
import org.dimas.medextest.feature.more.devsettings.presentation.util.label

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun FailureModeSettingsCard(
    selected: FailureMode,
    onSelect: (FailureMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsCard(title = "Ошибки сети", modifier = modifier) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FailureMode.entries.forEach { mode ->
                SettingChoice(mode.label(), selected = selected == mode, onClick = { onSelect(mode) })
            }
        }
    }
}
