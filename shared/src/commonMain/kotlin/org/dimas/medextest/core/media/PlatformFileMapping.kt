package org.dimas.medextest.core.media

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.mimeType
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.size

internal fun PlatformFile.toPickedFile(): PickedFile = PickedFile(
    name = name,
    mimeType = mimeType()?.toString(),
    sizeBytes = size(),
    reader = { readBytes() },
    platformHandle = this,
)
