package org.dimas.medextest.feature.more.loadlab.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.more.devsettings.presentation.ui.SettingChoice
import org.dimas.medextest.feature.more.loadlab.presentation.util.description
import org.dimas.medextest.feature.more.loadlab.presentation.util.label

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ModeSelector(
    modes: List<ExecutionMode>,
    selected: ExecutionMode,
    onSelect: (ExecutionMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            modes.forEach { mode ->
                SettingChoice(
                    label = mode.label,
                    selected = mode == selected,
                    onClick = { onSelect(mode) },
                    modifier = Modifier.testTag(LoadLabTags.mode(mode)),
                )
            }
        }
        Text(selected.description, style = MaterialTheme.typography.bodySmall, color = MedExColors.InkSecondary)
    }
}
