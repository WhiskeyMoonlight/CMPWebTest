package org.dimas.medextest.feature.onboarding.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.PrimaryButton
import org.dimas.medextest.core.designsystem.component.UnderlinedTextButton
import org.dimas.medextest.core.designsystem.illustration.onboardingBackground
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.core.designsystem.theme.MedExTheme
import org.dimas.medextest.feature.onboarding.presentation.mvi.OnboardingActions

private val ContentMaxWidth = 480.dp
private val ButtonMaxWidth = 400.dp

@Composable
internal fun OnboardingScreen(
    actions: OnboardingActions,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .onboardingBackground(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = ContentMaxWidth)
                .fillMaxWidth()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(1f))
            OnboardingAppIcon()
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Встречайте приложение MedEx 👋",
                style = MaterialTheme.typography.headlineLarge,
                color = MedExColors.Ink,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Персональные рекомендации\nна основе ваших данных",
                style = MaterialTheme.typography.bodyLarge,
                color = MedExColors.Ink,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.weight(1f))
            PrimaryButton(
                text = "Далее",
                onClick = actions::onNextClick,
                modifier = Modifier.widthIn(max = ButtonMaxWidth),
            )
            Spacer(Modifier.height(8.dp))
            UnderlinedTextButton(text = "Уже есть аккаунт?", onClick = actions::onHaveAccountClick)
            Spacer(Modifier.height(8.dp))
        }
    }
}

private object PreviewOnboardingActions : OnboardingActions {
    override fun onNextClick() = Unit
    override fun onHaveAccountClick() = Unit
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    MedExTheme { OnboardingScreen(actions = PreviewOnboardingActions) }
}
