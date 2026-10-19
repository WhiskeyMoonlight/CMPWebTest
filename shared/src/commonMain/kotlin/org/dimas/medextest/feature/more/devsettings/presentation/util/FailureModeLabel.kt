package org.dimas.medextest.feature.more.devsettings.presentation.util

import org.dimas.medextest.core.mock.FailureMode

internal fun FailureMode.label(): String = when (this) {
    FailureMode.Never -> "Нет"
    FailureMode.Sometimes -> "30% запросов"
    FailureMode.Always -> "Всегда"
}
