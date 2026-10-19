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
import org.dimas.medextest.core.designsystem.illustration.MoodBadge
import org.dimas.medextest.feature.checkin.domain.model.Mood
import org.dimas.medextest.feature.checkin.presentation.util.face
import org.dimas.medextest.feature.checkin.presentation.util.label

@Composable
internal fun MoodSelector(
    selected: Mood?,
    onSelect: (Mood) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth()) {
        Mood.entries.forEach { mood ->
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                MoodBadge(
                    face = mood.face(),
                    selected = selected == mood,
                    size = 54.dp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .selectable(selected = selected == mood, role = Role.RadioButton) { onSelect(mood) }
                        .semantics { contentDescription = mood.label() },
                )
            }
        }
    }
}
