package org.dimas.medextest.core.platform

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import org.koin.compose.koinInject

@Composable
actual fun BindPlatformToUi() {
    val requester = koinInject<AndroidPermissionRequester>()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        requester.onResult(result)
    }
    DisposableEffect(requester, launcher) {
        requester.attach(launcher)
        onDispose { requester.detach(launcher) }
    }
}
