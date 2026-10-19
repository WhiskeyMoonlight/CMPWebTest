package org.dimas.medextest.feature.more.presentation.mvi

import androidx.compose.runtime.Immutable
import org.dimas.medextest.feature.profile.domain.model.Profile

@Immutable
data class MoreState(val profile: Profile? = null)
