package org.dimas.medextest.feature.checkin.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.PrimaryButton
import org.dimas.medextest.core.designsystem.component.SoftButton
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.checkin.presentation.mvi.CheckInActions
import org.dimas.medextest.feature.checkin.presentation.mvi.CheckInState

@Composable
internal fun CheckInContent(
    state: CheckInState,
    note: TextFieldState,
    actions: CheckInActions,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 16.dp),
    ) {
        CheckInSectionTitle("Какое у вас настроение?")
        MoodSelector(selected = state.mood, onSelect = actions::onMoodSelect)
        Spacer(Modifier.height(24.dp))
        CheckInSectionTitle("Как вы себя сегодня чувствуете?")
        WellbeingSelector(selected = state.wellbeing, onSelect = actions::onWellbeingSelect)
        Spacer(Modifier.height(24.dp))
        CheckInSectionTitle("Симптомы")
        CheckInSymptoms(
            symptoms = state.symptoms,
            selectedIds = state.selectedSymptomIds,
            onToggle = actions::onSymptomToggle,
            onRetry = actions::onRetrySymptoms,
        )
        Spacer(Modifier.height(16.dp))
        CheckInNoteField(state = note)
        if (state.submitFailed) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Не удалось сохранить отметку. Попробуйте ещё раз",
                style = MaterialTheme.typography.bodySmall,
                color = MedExColors.Red,
            )
        }
        Spacer(Modifier.height(16.dp))
        if (state.canSubmit) {
            PrimaryButton(text = "Сохранить", onClick = actions::onPrimaryClick, loading = state.isSubmitting)
        } else {
            SoftButton(text = "Отмена", onClick = actions::onPrimaryClick)
        }
    }
}
