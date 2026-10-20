package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import org.dimas.medextest.core.common.text
import org.dimas.medextest.feature.checkin.presentation.CheckInViewModel
import org.dimas.medextest.feature.checkin.presentation.ui.CheckInSheet
import org.dimas.medextest.feature.home.presentation.HomeViewModel
import org.dimas.medextest.feature.home.presentation.mvi.HomeSideEffect
import org.dimas.medextest.feature.home.presentation.util.telUri
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeRoute(
    onOpenSearch: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    checkInViewModel: CheckInViewModel = koinViewModel(),
) {
    val state by viewModel.collectAsState()
    LaunchedEffect(checkInViewModel) { checkInViewModel.warmUp() }
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current
    viewModel.collectSideEffect { effect ->
        when (effect) {
            is HomeSideEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message.text())
        }
    }
    HomeScreen(
        state = state,
        actions = viewModel,
        snackbarHostState = snackbarHostState,
        onSearchClick = onOpenSearch,
        onCallClinic = { phone -> runCatching { uriHandler.openUri(telUri(phone)) } },
    )
    state.checkIn?.let { launch ->
        CheckInSheet(
            requestId = launch.requestId,
            preselectedMood = launch.preselectedMood,
            onClosed = viewModel::onCheckInClosed,
            viewModel = checkInViewModel,
        )
    }
}
