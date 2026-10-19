package org.dimas.medextest.core.format

import kotlin.time.Duration
import kotlin.time.DurationUnit

private const val MEGABYTE = 1024.0 * 1024.0

fun formatThroughput(bytes: Long, elapsed: Duration): String {
    val seconds = elapsed.toDouble(DurationUnit.SECONDS)
    if (seconds <= 0.0) return "—"
    return "${(bytes / MEGABYTE / seconds).toInt()}${NBSP}МБ/с"
}
