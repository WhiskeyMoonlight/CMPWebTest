package org.dimas.medextest.feature.more.capabilities.presentation.model

import org.dimas.medextest.core.location.GeoPoint

sealed interface LocationUi {
    data object Idle : LocationUi

    data object Requesting : LocationUi

    data class Located(val point: GeoPoint, val elapsedMs: Long) : LocationUi

    data class Failed(val reason: String) : LocationUi
}
