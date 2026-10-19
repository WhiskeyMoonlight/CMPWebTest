package org.dimas.medextest.feature.more.capabilities.presentation.model

import androidx.compose.ui.graphics.ImageBitmap

sealed interface AttachmentUi {
    data object Idle : AttachmentUi

    data class Reading(val name: String) : AttachmentUi

    data class Ready(
        val name: String,
        val mimeType: String?,
        val sizeBytes: Long,
        val preview: ImageBitmap?,
        val readMs: Long,
        val decodeMs: Long?,
    ) : AttachmentUi

    data class Failed(val reason: String) : AttachmentUi
}
