package org.dimas.medextest.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MedExTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MedExColorScheme,
        typography = MedExTypography,
        content = content,
    )
}
