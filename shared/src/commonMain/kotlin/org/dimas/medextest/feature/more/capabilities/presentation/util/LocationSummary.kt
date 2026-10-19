package org.dimas.medextest.feature.more.capabilities.presentation.util

import org.dimas.medextest.feature.more.capabilities.presentation.model.LocationUi

internal fun LocationUi.Located.summary(): String {
    val accuracy = point.accuracyMeters?.let { " ±${it.toInt()} м" }.orEmpty()
    return "${point.latitude.format()}, ${point.longitude.format()}$accuracy\nЗа $elapsedMs мс"
}
