package org.dimas.medextest.feature.more.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import org.dimas.medextest.core.common.text
import org.dimas.medextest.feature.more.presentation.MoreViewModel
import org.dimas.medextest.feature.more.presentation.mvi.MoreSideEffect
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MoreRoute(
    onOpenCapabilities: () -> Unit,
    onOpenDevSettings: () -> Unit,
    viewModel: MoreViewModel = koinViewModel(),
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    viewModel.collectSideEffect { effect ->
        when (effect) {
            is MoreSideEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message.text())
        }
    }
    MoreScreen(
        state = state,
        actions = viewModel,
        snackbarHostState = snackbarHostState,
        onOpenCapabilities = onOpenCapabilities,
        onOpenDevSettings = onOpenDevSettings,
    )
}
