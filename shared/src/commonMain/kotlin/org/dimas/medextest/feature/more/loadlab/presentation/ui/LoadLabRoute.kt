package org.dimas.medextest.feature.more.loadlab.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import org.dimas.medextest.core.media.PickKind
import org.dimas.medextest.core.media.rememberFilePickLauncher
import org.dimas.medextest.feature.more.loadlab.presentation.LoadLabViewModel
import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabSideEffect
import org.dimas.medextest.feature.more.loadlab.presentation.util.toMarkdownReport
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoadLabRoute(
    snackbarHostState: SnackbarHostState,
    viewModel: LoadLabViewModel = koinViewModel(),
) {
    val state by viewModel.collectAsState()
    viewModel.collectSideEffect { effect ->
        when (effect) {
            is LoadLabSideEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.text)
        }
    }
    val filePicker = rememberFilePickLauncher(PickKind.AnyFile, onResult = viewModel::onFilePicked, onError = viewModel::onPickerError)
    @Suppress("DEPRECATION")
    val clipboard = LocalClipboardManager.current

    LoadLabSection(
        state = state,
        actions = viewModel,
        onPickFile = filePicker::launch,
        onCopyReport = {
            clipboard.setText(AnnotatedString(state.toMarkdownReport()))
            viewModel.onReportCopied()
        },
    )
}
