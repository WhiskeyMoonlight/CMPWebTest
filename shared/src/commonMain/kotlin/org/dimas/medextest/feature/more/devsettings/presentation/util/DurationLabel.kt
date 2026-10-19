package org.dimas.medextest.feature.more.devsettings.presentation.util

import kotlin.time.Duration

private const val MILLIS_IN_SECOND = 1000

internal fun Duration.label(): String =
    if (inWholeMilliseconds >= MILLIS_IN_SECOND) "${inWholeMilliseconds / MILLIS_IN_SECOND} с" else "$inWholeMilliseconds мс"
