package org.dimas.medextest

import androidx.compose.ui.window.ComposeUIViewController
import org.dimas.medextest.app.MedExApp
import org.dimas.medextest.app.initApp
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initApp()
    return ComposeUIViewController { MedExApp() }
}
