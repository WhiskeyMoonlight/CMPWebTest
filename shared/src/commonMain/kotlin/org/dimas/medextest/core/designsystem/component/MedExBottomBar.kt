package org.dimas.medextest.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.theme.MedExColors

private val BottomBarHeight = 64.dp

@Composable
fun MedExBottomBar(
    items: List<NavigationItem>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    onAssistantClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val half = items.size / 2
    Column(modifier = modifier.fillMaxWidth().background(MedExColors.Surface).navigationBarsPadding()) {
        HorizontalDivider(color = MedExColors.Divider)
        Row(
            modifier = Modifier.fillMaxWidth().height(BottomBarHeight),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.take(half).forEachIndexed { index, item ->
                BottomBarItem(item = item, selected = index == selectedIndex, onClick = { onItemClick(index) })
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                AssistantButton(onClick = onAssistantClick)
            }
            items.drop(half).forEachIndexed { offset, item ->
                val index = half + offset
                BottomBarItem(item = item, selected = index == selectedIndex, onClick = { onItemClick(index) })
            }
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(item: NavigationItem, selected: Boolean, onClick: () -> Unit) {
    NavigationItemButton(
        item = item,
        selected = selected,
        onClick = onClick,
        modifier = Modifier.weight(1f).fillMaxHeight(),
    )
}
