package org.dimas.medextest.feature.home.presentation.model

import androidx.compose.runtime.Immutable
import org.dimas.medextest.feature.checkin.domain.model.Mood

@Immutable
data class CheckInLaunch(val requestId: Int, val preselectedMood: Mood?)
