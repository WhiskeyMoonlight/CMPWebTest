package org.dimas.medextest.feature.more.capabilities.presentation.util

import org.dimas.medextest.core.format.formatBytes
import org.dimas.medextest.feature.more.capabilities.presentation.model.AttachmentUi

internal fun AttachmentUi.Ready.summary(): String = buildString {
    append("$name · ${formatBytes(sizeBytes)}")
    mimeType?.let { append(" · $it") }
    append("\nЧтение: $readMs мс")
    decodeMs?.let { append(" · декодирование: $it мс") }
}
