package org.dimas.medextest.core.format

import kotlin.time.Duration

private const val MILLIS_IN_SECOND = 1000
private const val HUNDREDTHS = 10

fun formatDuration(duration: Duration): String {
    val millis = duration.inWholeMilliseconds
    if (millis < MILLIS_IN_SECOND) return "$millis${NBSP}мс"
    val seconds = millis / MILLIS_IN_SECOND
    val hundredths = (millis % MILLIS_IN_SECOND) / HUNDREDTHS
    return "$seconds,${hundredths.toString().padStart(2, '0')}${NBSP}с"
}
