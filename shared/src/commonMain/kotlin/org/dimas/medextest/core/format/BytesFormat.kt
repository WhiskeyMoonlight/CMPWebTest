package org.dimas.medextest.core.format

private const val KILOBYTE = 1024.0
private const val MEGABYTE = KILOBYTE * KILOBYTE

fun formatBytes(bytes: Long): String = when {
    bytes < KILOBYTE -> "$bytes${NBSP}Б"
    bytes < MEGABYTE -> "${(bytes / KILOBYTE).toInt()}${NBSP}КБ"
    else -> "${oneDecimal(bytes / MEGABYTE)}${NBSP}МБ"
}

private fun oneDecimal(value: Double): String {
    val tenths = (value * 10).toLong()
    val whole = tenths / 10
    val fraction = tenths % 10
    return if (fraction == 0L) whole.toString() else "$whole,$fraction"
}
