package org.dimas.medextest.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import io.github.vinceglb.filekit.dialogs.compose.rememberCameraPickerLauncher

@Composable
actual fun rememberCameraLauncher(
    onResult: (PickedFile?) -> Unit,
    onError: (Throwable) -> Unit,
): MediaLauncher {
    val currentOnResult by rememberUpdatedState(onResult)
    val currentOnError by rememberUpdatedState(onError)
    val launcher = rememberCameraPickerLauncher(
        onError = { currentOnError(it) },
        onResult = { file -> currentOnResult(file?.toPickedFile()) },
    )
    return remember(launcher) { MediaLauncher { launcher.launch() } }
}
