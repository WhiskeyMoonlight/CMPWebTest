package org.dimas.medextest.feature.checkin.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.icon.MedExIcons
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
internal fun CheckInNoteField(state: TextFieldState, modifier: Modifier = Modifier) {
    OutlinedTextField(
        state = state,
        modifier = modifier.fillMaxWidth().heightIn(min = 120.dp),
        placeholder = { Text("Поделитесь своими мыслями..", color = MedExColors.InkTertiary) },
        trailingIcon = { Icon(MedExIcons.Pencil, contentDescription = null, tint = MedExColors.InkSecondary) },
        lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 3, maxHeightInLines = 6),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MedExColors.Outline,
            focusedBorderColor = MedExColors.Teal,
            cursorColor = MedExColors.Teal,
        ),
    )
}
