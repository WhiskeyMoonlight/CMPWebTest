package org.dimas.medextest.feature.more.capabilities.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.MenuGroup
import org.dimas.medextest.core.designsystem.component.MenuRow
import org.dimas.medextest.core.designsystem.icon.MedExIcons
import org.dimas.medextest.feature.more.capabilities.presentation.util.PROMO_CODE

@Composable
internal fun SystemActionsGroup(
    onOpenMap: () -> Unit,
    onCall: () -> Unit,
    onCopyPromo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MenuGroup(modifier.padding(horizontal = 16.dp)) {
        MenuRow(title = "Открыть на карте", icon = MedExIcons.Map, onClick = onOpenMap)
        MenuRow(title = "Позвонить в клинику", icon = MedExIcons.Phone, onClick = onCall)
        MenuRow(title = "Скопировать промокод $PROMO_CODE", icon = MedExIcons.Copy, onClick = onCopyPromo, showDivider = false)
    }
}
