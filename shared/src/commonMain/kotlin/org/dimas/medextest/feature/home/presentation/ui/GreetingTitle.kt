package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.home.domain.model.Greeting
import org.dimas.medextest.feature.home.presentation.util.text

@Composable
internal fun GreetingTitle(greeting: Greeting?, modifier: Modifier = Modifier) {
    Text(
        text = greeting?.text() ?: " ",
        style = MaterialTheme.typography.headlineLarge,
        color = MedExColors.Ink,
        modifier = modifier,
    )
}
