package org.dimas.medextest.feature.more.loadlab.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.feature.more.devsettings.presentation.ui.SettingChoice
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.presentation.util.label

@Composable
internal fun ScaleSelector(
    selected: LoadScale,
    onSelect: (LoadScale) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        LoadScale.entries.forEach { scale ->
            SettingChoice(label = scale.label, selected = scale == selected, onClick = { onSelect(scale) })
        }
    }
}
