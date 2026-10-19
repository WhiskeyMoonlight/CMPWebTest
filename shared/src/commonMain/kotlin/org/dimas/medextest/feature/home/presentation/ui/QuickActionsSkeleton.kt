package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.SkeletonBox
import org.dimas.medextest.feature.home.domain.model.QuickAction

@Composable
internal fun QuickActionsSkeleton(modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(QuickAction.entries.size) {
            SkeletonBox(modifier = Modifier.weight(1f).height(118.dp))
        }
    }
}
