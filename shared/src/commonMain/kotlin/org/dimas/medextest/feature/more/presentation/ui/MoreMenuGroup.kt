package org.dimas.medextest.feature.more.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.dimas.medextest.core.designsystem.component.MenuGroup
import org.dimas.medextest.core.designsystem.component.MenuRow
import org.dimas.medextest.feature.more.presentation.model.MoreMenuItem

@Composable
internal fun MoreMenuGroup(
    items: List<MoreMenuItem>,
    onItemClick: (MoreMenuItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    MenuGroup(modifier) {
        items.forEachIndexed { index, item ->
            MenuRow(title = item.title, onClick = { onItemClick(item) }, showDivider = index != items.lastIndex)
        }
    }
}
