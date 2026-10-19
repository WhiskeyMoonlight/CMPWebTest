package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.WindowWidthClass
import org.dimas.medextest.core.designsystem.component.MenuRow
import org.dimas.medextest.feature.marketplace.domain.model.ServiceCategory

@Composable
internal fun ServicesList(items: List<ServiceCategory>, onItemClick: () -> Unit) {
    if (LocalWindowWidthClass.current == WindowWidthClass.Expanded) {
        val leftColumnSize = (items.size + 1) / 2
        Row {
            ServicesColumn(items.take(leftColumnSize), onItemClick, Modifier.weight(1f))
            ServicesColumn(items.drop(leftColumnSize), onItemClick, Modifier.weight(1f))
        }
    } else {
        ServicesColumn(items, onItemClick)
    }
}

@Composable
private fun ServicesColumn(
    items: List<ServiceCategory>,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        items.forEachIndexed { index, category ->
            MenuRow(title = category.title, onClick = onItemClick, showDivider = index != items.lastIndex)
        }
    }
}
