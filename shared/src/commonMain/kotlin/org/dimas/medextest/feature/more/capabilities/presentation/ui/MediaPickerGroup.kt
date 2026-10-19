package org.dimas.medextest.feature.more.capabilities.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.MenuGroup
import org.dimas.medextest.core.designsystem.component.MenuRow
import org.dimas.medextest.core.designsystem.icon.MedExIcons

@Composable
internal fun MediaPickerGroup(
    onPickImage: () -> Unit,
    onPickFile: () -> Unit,
    onTakePhoto: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MenuGroup(modifier.padding(horizontal = 16.dp)) {
        MenuRow(title = "Фото из галереи", icon = MedExIcons.Image, onClick = onPickImage)
        MenuRow(title = "Любой файл", icon = MedExIcons.File, onClick = onPickFile)
        MenuRow(title = "Снять фото", icon = MedExIcons.Camera, onClick = onTakePhoto, showDivider = false)
    }
}
