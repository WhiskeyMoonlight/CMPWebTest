package org.dimas.medextest.feature.more.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.MenuGroup
import org.dimas.medextest.core.designsystem.component.MenuRow
import org.dimas.medextest.core.designsystem.icon.MedExIcons
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
internal fun TechDemoSection(
    onOpenCapabilities: () -> Unit,
    onOpenDevSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = "Технологическое демо",
            style = MaterialTheme.typography.labelMedium,
            color = MedExColors.InkSecondary,
            modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp),
        )
        MenuGroup {
            MenuRow(
                title = "Возможности устройства",
                subtitle = "Файлы, камера, геолокация, звонок",
                icon = MedExIcons.MapPin,
                onClick = onOpenCapabilities,
            )
            MenuRow(
                title = "Для разработчика",
                subtitle = "Задержки, ошибки и пустые ответы моков",
                icon = MedExIcons.Sliders,
                onClick = onOpenDevSettings,
                showDivider = false,
            )
        }
    }
}
