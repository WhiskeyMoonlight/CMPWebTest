package org.dimas.medextest.feature.more.capabilities.presentation.mvi

import androidx.compose.runtime.Immutable
import org.dimas.medextest.core.platform.PlatformInfo
import org.dimas.medextest.feature.more.capabilities.presentation.model.AttachmentUi
import org.dimas.medextest.feature.more.capabilities.presentation.model.LocationUi

@Immutable
data class CapabilitiesState(
    val platform: PlatformInfo,
    val location: LocationUi = LocationUi.Idle,
    val attachment: AttachmentUi = AttachmentUi.Idle,
)
