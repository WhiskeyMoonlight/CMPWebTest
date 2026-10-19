package org.dimas.medextest.feature.more.capabilities.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import org.dimas.medextest.core.media.PickKind
import org.dimas.medextest.core.media.rememberCameraLauncher
import org.dimas.medextest.core.media.rememberFilePickLauncher
import org.dimas.medextest.feature.more.capabilities.presentation.CapabilitiesViewModel
import org.dimas.medextest.feature.more.capabilities.presentation.mvi.CapabilitiesSideEffect
import org.dimas.medextest.feature.more.capabilities.presentation.util.CLINIC_PHONE
import org.dimas.medextest.feature.more.capabilities.presentation.util.PROMO_CODE
import org.dimas.medextest.feature.more.capabilities.presentation.util.mapUrl
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CapabilitiesRoute(
    onBack: () -> Unit,
    viewModel: CapabilitiesViewModel = koinViewModel(),
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    viewModel.collectSideEffect { effect ->
        when (effect) {
            is CapabilitiesSideEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.text)
        }
    }

    val imagePicker = rememberFilePickLauncher(PickKind.Image, onResult = viewModel::onFilePicked, onError = viewModel::onPickerError)
    val filePicker = rememberFilePickLauncher(PickKind.AnyFile, onResult = viewModel::onFilePicked, onError = viewModel::onPickerError)
    val camera = rememberCameraLauncher(onResult = viewModel::onFilePicked, onError = viewModel::onPickerError)
    val uriHandler = LocalUriHandler.current
    @Suppress("DEPRECATION")
    val clipboard = LocalClipboardManager.current

    CapabilitiesScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onPickImage = imagePicker::launch,
        onPickFile = filePicker::launch,
        onTakePhoto = camera::launch,
        onLocate = viewModel::onLocateClick,
        onOpenMap = { runCatching { uriHandler.openUri(mapUrl(state.location)) } },
        onCall = { runCatching { uriHandler.openUri("tel:$CLINIC_PHONE") } },
        onCopyPromo = {
            clipboard.setText(AnnotatedString(PROMO_CODE))
            viewModel.onPromoCopied()
        },
    )
}
