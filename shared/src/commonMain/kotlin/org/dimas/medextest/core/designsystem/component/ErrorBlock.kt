package org.dimas.medextest.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.common.AppError
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
fun ErrorBlock(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = error.userMessage(),
            style = MaterialTheme.typography.bodyMedium,
            color = MedExColors.InkSecondary,
            textAlign = TextAlign.Center,
        )
        OutlinedPillButton(text = "Повторить", onClick = onRetry)
    }
}
