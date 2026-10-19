package org.dimas.medextest.feature.more.capabilities.presentation.util

import org.dimas.medextest.core.media.PickedFile

private val IMAGE_EXTENSIONS = setOf("jpg", "jpeg", "png", "webp", "gif", "bmp")

internal fun PickedFile.isImage(): Boolean =
    mimeType?.startsWith("image/") == true || name.substringAfterLast('.').lowercase() in IMAGE_EXTENSIONS
