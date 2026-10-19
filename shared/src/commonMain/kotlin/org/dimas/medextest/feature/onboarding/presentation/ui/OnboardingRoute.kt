package org.dimas.medextest.feature.onboarding.presentation.ui

import androidx.compose.runtime.Composable
import org.dimas.medextest.feature.onboarding.presentation.OnboardingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingRoute(viewModel: OnboardingViewModel = koinViewModel()) {
    OnboardingScreen(actions = viewModel)
}
