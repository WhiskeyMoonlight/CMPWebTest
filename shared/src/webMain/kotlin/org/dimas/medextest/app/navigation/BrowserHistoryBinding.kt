package org.dimas.medextest.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow

@Composable
fun BrowserHistoryBinding(navigator: AppNavigator) {
    val sync = remember(navigator) { BrowserHistorySync(navigator, appDeepLinks) }
    DisposableEffect(sync) {
        sync.start()
        onDispose { sync.stop() }
    }
    LaunchedEffect(sync) {
        snapshotFlow { navigator.currentKey }.collect(sync::onKeyChanged)
    }
}
