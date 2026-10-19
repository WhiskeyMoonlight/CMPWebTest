package org.dimas.medextest.feature.chat.presentation.ui

import androidx.compose.runtime.Composable
import org.dimas.medextest.core.designsystem.component.PlaceholderContent

@Composable
fun ChatRoute(onBack: () -> Unit) {
    PlaceholderContent(
        title = "AI-ассистент",
        description = "Здесь будет чат с ассистентом на GigaChat",
        onBack = onBack,
    )
}
