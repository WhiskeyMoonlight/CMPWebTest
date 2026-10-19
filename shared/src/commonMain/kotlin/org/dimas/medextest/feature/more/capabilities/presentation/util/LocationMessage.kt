package org.dimas.medextest.feature.more.capabilities.presentation.util

import org.dimas.medextest.core.location.LocationException

internal fun Throwable.toLocationMessage(): String = when (this) {
    is LocationException.PermissionDenied -> "Доступ к геопозиции запрещён. Разрешите его в настройках"
    is LocationException.Disabled -> "Геолокация выключена на устройстве"
    is LocationException.Timeout -> "Не удалось определить позицию за отведённое время"
    is LocationException.Unavailable -> "Позиция недоступна: $message"
    else -> message ?: "Неизвестная ошибка"
}
