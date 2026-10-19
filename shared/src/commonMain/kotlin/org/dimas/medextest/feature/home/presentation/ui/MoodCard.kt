package org.dimas.medextest.feature.home.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.SurfaceCard
import org.dimas.medextest.core.designsystem.illustration.MoodBadge
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.checkin.domain.model.Mood
import org.dimas.medextest.feature.checkin.presentation.util.face
import org.dimas.medextest.feature.checkin.presentation.util.label

@Composable
internal fun MoodCard(onMoodClick: (Mood) -> Unit, modifier: Modifier = Modifier) {
    SurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(text = "Как Вы себя сегодня чувствуете?", style = MaterialTheme.typography.titleSmall, color = MedExColors.Ink)
            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth()) {
                Mood.entries.forEach { mood ->
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        MoodBadge(
                            face = mood.face(),
                            size = 54.dp,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable(role = Role.Button) { onMoodClick(mood) }
                                .semantics { contentDescription = mood.label() },
                        )
                    }
                }
            }
        }
    }
}
