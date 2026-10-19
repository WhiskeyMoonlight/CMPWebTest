package org.dimas.medextest.core.compute

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.WebFile
import js.buffer.ArrayBuffer
import js.promise.await
import js.reflect.unsafeCast
import org.dimas.medextest.core.media.BrowserFileHandle
import org.dimas.medextest.core.media.PickedFile
import web.file.File

internal suspend fun PickedFile.arrayBuffer(): ArrayBuffer? {
    val file: File = when (val handle = platformHandle) {
        is BrowserFileHandle -> handle.file
        is PlatformFile -> (handle.webFile as? WebFile.FileWrapper)?.file?.let { unsafeCast<File>(it) }
        else -> null
    } ?: return null
    return file.arrayBufferAsync().await()
}
