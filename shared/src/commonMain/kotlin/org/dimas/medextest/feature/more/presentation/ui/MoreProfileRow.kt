package org.dimas.medextest.feature.more.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.icon.MedExIcons
import org.dimas.medextest.core.designsystem.illustration.Avatar
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.profile.domain.model.Profile

@Composable
internal fun MoreProfileRow(
    profile: Profile?,
    modifier: Modifier = Modifier,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Avatar(initials = profile?.initials.orEmpty(), seed = profile?.avatarSeed ?: 0, size = 48.dp)
        Spacer(Modifier.width(12.dp))
        Text(profile?.shortName ?: "…", style = MaterialTheme.typography.titleMedium, color = MedExColors.Ink)
        Icon(MedExIcons.ChevronDown, contentDescription = null, tint = MedExColors.InkSecondary, modifier = Modifier.size(20.dp))
    }
}
