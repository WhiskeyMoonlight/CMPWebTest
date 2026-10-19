package org.dimas.medextest.feature.checkin.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.SkeletonBox
import org.dimas.medextest.feature.checkin.presentation.util.COLLAPSED_SYMPTOMS

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SymptomsSkeleton(modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(COLLAPSED_SYMPTOMS) {
            SkeletonBox(Modifier.width(90.dp).height(28.dp), shape = RoundedCornerShape(14.dp))
        }
    }
}
