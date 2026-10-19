package org.dimas.medextest.feature.more.loadlab.presentation.model

enum class TaskUiStatus(val label: String) {
    Running("Выполняется"),
    Completed("Готово"),
    Failed("Ошибка"),
    Cancelled("Отменено"),
}
