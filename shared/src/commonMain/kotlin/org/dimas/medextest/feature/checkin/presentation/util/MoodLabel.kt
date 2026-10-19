package org.dimas.medextest.feature.checkin.presentation.util

import org.dimas.medextest.feature.checkin.domain.model.Mood

fun Mood.label(): String = when (this) {
    Mood.Awful -> "Ужасное настроение"
    Mood.Bad -> "Плохое настроение"
    Mood.Neutral -> "Обычное настроение"
    Mood.Good -> "Хорошее настроение"
    Mood.Great -> "Отличное настроение"
}
