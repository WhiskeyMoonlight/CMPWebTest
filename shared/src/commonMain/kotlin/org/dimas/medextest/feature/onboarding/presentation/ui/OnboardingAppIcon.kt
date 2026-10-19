package org.dimas.medextest.feature.onboarding.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.icon.LogoMark
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
internal fun OnboardingAppIcon(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier = modifier
            .size(96.dp)
            .shadow(elevation = 12.dp, shape = shape, ambientColor = MedExColors.Teal, spotColor = MedExColors.Teal)
            .clip(shape)
            .background(Color.White),
    ) {
        Image(imageVector = LogoMark, contentDescription = "Логотип MedEx", modifier = Modifier.fillMaxSize())
    }
}
