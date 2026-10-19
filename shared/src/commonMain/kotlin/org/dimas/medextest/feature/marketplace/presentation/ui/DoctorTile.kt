package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.illustration.StackedAvatars
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.core.format.formatPriceFrom
import org.dimas.medextest.feature.marketplace.domain.model.DoctorSpecialty

@Composable
internal fun DoctorTile(doctor: DoctorSpecialty, onClick: () -> Unit) {
    OutlinedTile(width = MarketplaceTileDefaults.Width, onClick = onClick) {
        Column(Modifier.padding(12.dp)) {
            StackedAvatars(seeds = doctor.doctorSeeds)
            Spacer(Modifier.height(10.dp))
            Text(doctor.title, style = MaterialTheme.typography.titleSmall, color = MedExColors.Ink)
            Text(formatPriceFrom(doctor.priceFrom), style = MaterialTheme.typography.bodySmall, color = MedExColors.InkSecondary)
        }
    }
}
