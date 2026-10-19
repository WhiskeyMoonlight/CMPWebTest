package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.feature.home.domain.model.QuickAction

@Composable
internal fun QuickActionsRow(
    quickActions: List<QuickAction>,
    onClick: (QuickAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        quickActions.forEach { action ->
            QuickActionCard(action = action, onClick = { onClick(action) }, modifier = Modifier.weight(1f))
        }
    }
}
