package org.dimas.medextest.feature.checkin.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.core.designsystem.component.ErrorBlock
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.checkin.domain.model.Symptom

@Composable
internal fun CheckInSymptoms(
    symptoms: LoadState<List<Symptom>>,
    selectedIds: Set<String>,
    onToggle: (String) -> Unit,
    onRetry: () -> Unit,
) {
    when (symptoms) {
        LoadState.Loading -> SymptomsSkeleton()
        LoadState.Empty -> Text(
            text = "Список симптомов пуст",
            style = MaterialTheme.typography.bodySmall,
            color = MedExColors.InkSecondary,
        )
        is LoadState.Error -> ErrorBlock(symptoms.error, onRetry = onRetry)
        is LoadState.Content -> SymptomChips(symptoms = symptoms.data, selected = selectedIds, onToggle = onToggle)
    }
}
