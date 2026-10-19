package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.OutlinedPillButton
import org.dimas.medextest.core.designsystem.component.SurfaceCard
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
internal fun HealthProfilePromo(onFillClick: () -> Unit, modifier: Modifier = Modifier) {
    SurfaceCard(modifier.fillMaxWidth()) {
        Box {
            Spacer(
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(110.dp)
                    .drawWithCache {
                        val blob = Brush.radialGradient(
                            listOf(MedExColors.TealLight.copy(alpha = 0.55f), Color.Transparent),
                            center = Offset(size.width * 0.65f, size.height * 0.65f),
                            radius = size.minDimension * 0.55f,
                        )
                        onDrawBehind { drawRect(blob) }
                    },
            )
            Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🔒 Разблокируйте персональные рекомендации",
                    style = MaterialTheme.typography.titleMedium,
                    color = MedExColors.Ink,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Я смогу подсказывать, как улучшить сон, активность, питание и стресс — как только Вы завершите первые шаги.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MedExColors.InkSecondary,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(12.dp))
                OutlinedPillButton(text = "Заполнить профиль здоровья", onClick = onFillClick)
            }
        }
    }
}
