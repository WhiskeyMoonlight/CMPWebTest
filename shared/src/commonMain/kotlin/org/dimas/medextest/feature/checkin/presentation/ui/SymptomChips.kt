package org.dimas.medextest.feature.checkin.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.checkin.domain.model.Symptom
import org.dimas.medextest.feature.checkin.presentation.util.COLLAPSED_SYMPTOMS
import org.dimas.medextest.feature.checkin.presentation.util.color

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SymptomChips(
    symptoms: List<Symptom>,
    selected: Set<String>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val visible = if (expanded) symptoms else symptoms.take(COLLAPSED_SYMPTOMS)
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        visible.forEach { symptom ->
            val isSelected = symptom.id in selected
            SymptomChip(
                text = symptom.title,
                color = symptom.tone.color(),
                selected = isSelected,
                modifier = Modifier.toggleable(value = isSelected, role = Role.Checkbox) { onToggle(symptom.id) },
            )
        }
        if (!expanded && symptoms.size > COLLAPSED_SYMPTOMS) {
            SymptomChip(
                text = "Ещё…",
                color = MedExColors.InkSecondary,
                selected = true,
                modifier = Modifier.toggleable(value = false, role = Role.Button) { expanded = true },
            )
        }
    }
}
