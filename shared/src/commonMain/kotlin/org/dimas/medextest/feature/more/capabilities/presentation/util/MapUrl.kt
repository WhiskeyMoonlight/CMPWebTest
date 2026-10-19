package org.dimas.medextest.feature.more.capabilities.presentation.util

import org.dimas.medextest.feature.more.capabilities.presentation.model.LocationUi

internal fun mapUrl(location: LocationUi): String = when (location) {
    is LocationUi.Located -> "https://yandex.ru/maps/?pt=${location.point.longitude},${location.point.latitude}&z=16&l=map"
    else -> CLINIC_MAP_URL
}
