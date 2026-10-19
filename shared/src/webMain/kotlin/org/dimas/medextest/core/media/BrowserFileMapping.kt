package org.dimas.medextest.core.media

import web.blob.byteArray
import web.file.File

internal fun File.toPickedFile(): PickedFile = PickedFile(
    name = name,
    mimeType = type.ifEmpty { null },
    sizeBytes = size.toLong(),
    reader = { byteArray() },
    platformHandle = BrowserFileHandle(this),
)
