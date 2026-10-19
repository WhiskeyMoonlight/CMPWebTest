package org.dimas.medextest.feature.more.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.dimas.medextest.feature.more.presentation.model.MoreMenuItem
import org.dimas.medextest.feature.profile.domain.model.Profile

@Composable
internal fun MoreTwoColumnContent(
    profile: Profile?,
    onMenuItemClick: (MoreMenuItem) -> Unit,
    onOpenCapabilities: () -> Unit,
    onOpenDevSettings: () -> Unit,
    contentMaxWidth: Dp,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        MoreHeader(profile = profile, contentMaxWidth = contentMaxWidth) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ColumnSpacing),
            ) {
                MoreMenuGroup(
                    items = MoreMenuItem.accountItems,
                    onItemClick = onMenuItemClick,
                    modifier = Modifier.weight(1f),
                )
                Column(Modifier.weight(1f)) {
                    MoreMenuGroup(items = MoreMenuItem.clinicItems, onItemClick = onMenuItemClick)
                    TechDemoSection(onOpenCapabilities = onOpenCapabilities, onOpenDevSettings = onOpenDevSettings)
                }
            }
        }
    }
}

private val ColumnSpacing = 16.dp
