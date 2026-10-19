package org.dimas.medextest.feature.more.capabilities.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.contentMaxWidth
import org.dimas.medextest.core.designsystem.component.BackTopBar
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.more.capabilities.presentation.mvi.CapabilitiesState

@Composable
fun CapabilitiesScreen(
    state: CapabilitiesState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onPickImage: () -> Unit,
    onPickFile: () -> Unit,
    onTakePhoto: () -> Unit,
    onLocate: () -> Unit,
    onOpenMap: () -> Unit,
    onCall: () -> Unit,
    onCopyPromo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentMaxWidth = LocalWindowWidthClass.current.contentMaxWidth
    Box(modifier.fillMaxSize().background(MedExColors.Background)) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(Modifier.widthIn(max = contentMaxWidth).fillMaxWidth()) {
                BackTopBar(title = "Возможности устройства", onBack = onBack)
                PlatformNote(platform = state.platform)

                SectionCaption("Файлы и камера")
                MediaPickerGroup(onPickImage = onPickImage, onPickFile = onPickFile, onTakePhoto = onTakePhoto)
                AttachmentResult(attachment = state.attachment)

                SectionCaption("Геолокация")
                LocationCard(location = state.location, onLocate = onLocate)

                SectionCaption("Системные действия")
                SystemActionsGroup(onOpenMap = onOpenMap, onCall = onCall, onCopyPromo = onCopyPromo)
                Spacer(Modifier.height(24.dp))
            }
        }
        SnackbarHost(snackbarHostState, Modifier.align(Alignment.BottomCenter))
    }
}
