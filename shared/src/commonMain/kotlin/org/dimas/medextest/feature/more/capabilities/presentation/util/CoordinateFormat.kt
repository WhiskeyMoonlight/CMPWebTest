package org.dimas.medextest.feature.more.capabilities.presentation.util

import kotlin.math.round

private const val COORDINATE_SCALE = 100_000

internal fun Double.format(): String {
    val scaled = round(this * COORDINATE_SCALE) / COORDINATE_SCALE
    return scaled.toString()
}
