package org.dimas.medextest.feature.checkin.presentation.util

import androidx.compose.ui.graphics.Color
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.checkin.domain.model.SymptomTone

internal fun SymptomTone.color(): Color = when (this) {
    SymptomTone.Positive -> MedExColors.Green
    SymptomTone.Calm -> MedExColors.Blue
    SymptomTone.Negative -> MedExColors.Red
    SymptomTone.Leisure -> MedExColors.Purple
    SymptomTone.Activity -> MedExColors.Orange
}
