package org.dimas.medextest.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
actual fun rememberCameraLauncher(
    onResult: (PickedFile?) -> Unit,
    onError: (Throwable) -> Unit,
): MediaLauncher {
    val currentOnResult by rememberUpdatedState(onResult)
    val currentOnError by rememberUpdatedState(onError)
    return remember {
        MediaLauncher {
            runCatching { openCaptureInput { file -> currentOnResult(file?.toPickedFile()) } }
                .onFailure { currentOnError(it) }
        }
    }
}
