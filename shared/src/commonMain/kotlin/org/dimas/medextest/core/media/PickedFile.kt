package org.dimas.medextest.core.media

class PickedFile(
    val name: String,
    val mimeType: String?,
    val sizeBytes: Long,
    private val reader: suspend () -> ByteArray,
    val platformHandle: Any? = null,
) {
    suspend fun readBytes(): ByteArray = reader()
}
