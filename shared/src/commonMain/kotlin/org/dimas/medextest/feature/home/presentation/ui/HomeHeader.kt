package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.feature.home.domain.model.Greeting
import org.dimas.medextest.feature.home.domain.model.HomeDashboard
import org.dimas.medextest.feature.home.presentation.mvi.HomeActions

@Composable
internal fun HomeHeader(
    greeting: Greeting?,
    dashboard: LoadState<HomeDashboard>,
    actions: HomeActions,
    onSearchClick: () -> Unit,
    onCallClinic: (phone: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        HomeHeaderBar(
            greeting = greeting,
            onSearchClick = onSearchClick,
            onCalendarClick = actions::onCalendarClick,
        )
        Spacer(Modifier.height(14.dp))
        GreetingTitle(greeting)
        Spacer(Modifier.height(16.dp))
        HomeDashboardBlock(dashboard = dashboard, actions = actions, onCallClinic = onCallClinic)
    }
}
