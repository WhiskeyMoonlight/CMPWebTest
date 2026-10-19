package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.core.designsystem.component.ErrorBlock
import org.dimas.medextest.core.designsystem.component.SurfaceCard
import org.dimas.medextest.feature.home.domain.model.HomeDashboard
import org.dimas.medextest.feature.home.domain.model.QuickAction
import org.dimas.medextest.feature.home.presentation.mvi.HomeActions

@Composable
internal fun HomeDashboardBlock(
    dashboard: LoadState<HomeDashboard>,
    actions: HomeActions,
    onCallClinic: (phone: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (dashboard) {
        LoadState.Loading -> QuickActionsSkeleton(modifier)
        LoadState.Empty -> Unit
        is LoadState.Error -> SurfaceCard(modifier.fillMaxWidth()) {
            ErrorBlock(dashboard.error, onRetry = actions::onRetry)
        }
        is LoadState.Content -> Column(modifier.fillMaxWidth()) {
            QuickActionsRow(
                quickActions = dashboard.data.quickActions,
                onClick = { action ->
                    if (action == QuickAction.CallClinic) onCallClinic(dashboard.data.clinicPhone) else actions.onQuickActionClick(action)
                },
            )
            if (dashboard.data.showHealthProfilePromo) {
                Spacer(Modifier.height(8.dp))
                HealthProfilePromo(onFillClick = actions::onFillHealthProfileClick)
            }
        }
    }
}
