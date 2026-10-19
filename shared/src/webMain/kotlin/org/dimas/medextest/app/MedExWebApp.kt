package org.dimas.medextest.app

import androidx.compose.runtime.Composable
import org.dimas.medextest.app.navigation.BrowserHistoryBinding

@Composable
fun MedExWebApp() {
    MedExApp(navigatorBinding = { navigator -> BrowserHistoryBinding(navigator) })
}
