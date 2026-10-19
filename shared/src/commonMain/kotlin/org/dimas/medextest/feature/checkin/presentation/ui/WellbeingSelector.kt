package org.dimas.medextest.feature.checkin.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.illustration.HeartBadge
import org.dimas.medextest.feature.checkin.domain.usecase.WELLBEING_MAX

@Composable
internal fun WellbeingSelector(
    selected: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth()) {
        (1..WELLBEING_MAX).forEach { value ->
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                HeartBadge(
                    filled = (selected ?: 0) >= value,
                    size = 54.dp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .selectable(selected = selected == value, role = Role.RadioButton) { onSelect(value) }
                        .semantics { contentDescription = "Самочувствие $value из $WELLBEING_MAX" },
                )
            }
        }
    }
}
