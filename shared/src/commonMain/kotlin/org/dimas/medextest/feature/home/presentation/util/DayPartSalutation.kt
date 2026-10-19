package org.dimas.medextest.feature.home.presentation.util

import org.dimas.medextest.feature.home.domain.model.DayPart

internal fun DayPart.salutation(): String = when (this) {
    DayPart.Morning -> "Доброе утро"
    DayPart.Day -> "Добрый день"
    DayPart.Evening -> "Добрый вечер"
    DayPart.Night -> "Доброй ночи"
}
