package org.dimas.medextest.feature.more.devsettings.presentation.ui

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
internal fun SettingChoice(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MedExColors.CyanSoft, selectedLabelColor = MedExColors.TealText),
    )
}
