package org.dimas.medextest.feature.checkin.presentation.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.dimas.medextest.feature.checkin.domain.model.Mood
import org.dimas.medextest.feature.checkin.presentation.CheckInViewModel
import org.dimas.medextest.feature.checkin.presentation.mvi.CheckInSideEffect
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInSheet(
    requestId: Int,
    preselectedMood: Mood?,
    onClosed: (submitted: Boolean) -> Unit,
    viewModel: CheckInViewModel = koinViewModel(),
) {
    val state by viewModel.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(requestId) {
        viewModel.note.clearText()
        viewModel.onOpened(preselectedMood)
    }
    viewModel.collectSideEffect { effect ->
        when (effect) {
            is CheckInSideEffect.Close -> scope.launch { sheetState.hide() }
                .invokeOnCompletion { onClosed(effect.submitted) }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onClosed(false) },
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    ) {
        CheckInContent(state = state, note = viewModel.note, actions = viewModel)
    }
}
