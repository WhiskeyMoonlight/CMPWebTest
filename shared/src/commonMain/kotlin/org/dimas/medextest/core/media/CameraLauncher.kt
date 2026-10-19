package org.dimas.medextest.core.media

import androidx.compose.runtime.Composable

@Composable
expect fun rememberCameraLauncher(
    onResult: (PickedFile?) -> Unit,
    onError: (Throwable) -> Unit,
): MediaLauncher
