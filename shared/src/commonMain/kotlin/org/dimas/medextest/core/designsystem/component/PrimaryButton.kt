package org.dimas.medextest.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    FilledButton(
        text = text,
        onClick = onClick,
        containerColor = MedExColors.Teal,
        contentColor = Color.White,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
    )
}
