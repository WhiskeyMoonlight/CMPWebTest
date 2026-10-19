package org.dimas.medextest

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import org.dimas.medextest.app.MedExWebApp
import org.dimas.medextest.app.initMedExWeb

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initMedExWeb()
    ComposeViewport {
        MedExWebApp()
    }
}
