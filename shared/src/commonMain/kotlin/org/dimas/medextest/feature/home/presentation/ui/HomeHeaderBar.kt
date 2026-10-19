package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.SquareIconButton
import org.dimas.medextest.core.designsystem.icon.MedExIcons
import org.dimas.medextest.core.designsystem.illustration.Avatar
import org.dimas.medextest.feature.home.domain.model.Greeting

@Composable
internal fun HomeHeaderBar(
    greeting: Greeting?,
    onSearchClick: () -> Unit,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Avatar(initials = greeting?.initials.orEmpty(), seed = greeting?.avatarSeed ?: 0, size = 56.dp)
        Spacer(Modifier.weight(1f))
        SquareIconButton(icon = MedExIcons.Search, contentDescription = "Поиск", onClick = onSearchClick)
        Spacer(Modifier.width(8.dp))
        SquareIconButton(icon = MedExIcons.Calendar, contentDescription = "Календарь", onClick = onCalendarClick)
    }
}
