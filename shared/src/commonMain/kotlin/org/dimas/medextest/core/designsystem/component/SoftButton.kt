package org.dimas.medextest.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
fun SoftButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
) {
    FilledButton(
        text = text,
        onClick = onClick,
        containerColor = MedExColors.CyanSoft,
        contentColor = MedExColors.TealText,
        modifier = modifier,
        loading = loading,
    )
}
