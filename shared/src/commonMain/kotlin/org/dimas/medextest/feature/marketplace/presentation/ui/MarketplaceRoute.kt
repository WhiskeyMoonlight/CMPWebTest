package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import org.dimas.medextest.core.common.text
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId
import org.dimas.medextest.feature.marketplace.presentation.MarketplaceViewModel
import org.dimas.medextest.feature.marketplace.presentation.mvi.MarketplaceSideEffect
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MarketplaceRoute(
    onOpenSection: (MarketplaceSectionId) -> Unit,
    viewModel: MarketplaceViewModel = koinViewModel(),
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    viewModel.collectSideEffect { effect ->
        when (effect) {
            is MarketplaceSideEffect.OpenSection -> onOpenSection(effect.id)
            is MarketplaceSideEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message.text())
        }
    }
    MarketplaceScreen(state = state, query = viewModel.query, actions = viewModel, snackbarHostState = snackbarHostState)
}
