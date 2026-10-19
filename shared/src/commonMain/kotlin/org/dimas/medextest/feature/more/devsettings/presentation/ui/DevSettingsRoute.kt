package org.dimas.medextest.feature.more.devsettings.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import org.dimas.medextest.feature.more.devsettings.presentation.DevSettingsViewModel
import org.dimas.medextest.feature.more.devsettings.presentation.mvi.DevSettingsSideEffect
import org.dimas.medextest.feature.more.loadlab.presentation.ui.LoadLabRoute
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun DevSettingsRoute(
    onBack: () -> Unit,
    viewModel: DevSettingsViewModel = koinViewModel(),
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    viewModel.collectSideEffect { effect ->
        when (effect) {
            is DevSettingsSideEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.text)
        }
    }
    DevSettingsScreen(
        state = state,
        actions = viewModel,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        loadLab = { LoadLabRoute(snackbarHostState) },
    )
}
