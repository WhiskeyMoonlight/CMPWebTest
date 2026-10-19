package org.dimas.medextest.feature.more.devsettings.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.OutlinedPillButton

@Composable
internal fun ResetSettingsCard(
    onResetOnboarding: () -> Unit,
    onResetCheckIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsCard(title = "Сброс", modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedPillButton(text = "Онбординг", onClick = onResetOnboarding)
            OutlinedPillButton(text = "Чек-ин", onClick = onResetCheckIn)
        }
    }
}
