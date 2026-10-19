package org.dimas.medextest.feature.more.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.dimas.medextest.feature.more.presentation.model.MoreMenuItem
import org.dimas.medextest.feature.profile.domain.model.Profile

@Composable
internal fun MoreSingleColumnContent(
    profile: Profile?,
    onMenuItemClick: (MoreMenuItem) -> Unit,
    onOpenCapabilities: () -> Unit,
    onOpenDevSettings: () -> Unit,
    contentMaxWidth: Dp,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item(key = "header") {
            MoreHeader(profile = profile, contentMaxWidth = contentMaxWidth) {
                MoreMenuGroup(items = MoreMenuItem.accountItems, onItemClick = onMenuItemClick)
            }
        }
        item(key = "clinic") {
            MoreMenuGroup(
                items = MoreMenuItem.clinicItems,
                onItemClick = onMenuItemClick,
                modifier = Modifier.widthIn(max = contentMaxWidth).padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }
        item(key = "tech") {
            TechDemoSection(
                onOpenCapabilities = onOpenCapabilities,
                onOpenDevSettings = onOpenDevSettings,
                modifier = Modifier.widthIn(max = contentMaxWidth).padding(horizontal = 16.dp),
            )
        }
    }
}
