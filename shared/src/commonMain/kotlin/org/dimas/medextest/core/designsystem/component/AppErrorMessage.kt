package org.dimas.medextest.core.designsystem.component

import org.dimas.medextest.core.common.AppError

fun AppError.userMessage(): String = when (this) {
    AppError.Network -> "Не удалось загрузить данные. Проверьте соединение"
    is AppError.Unexpected -> "Что-то пошло не так"
}
