package org.dimas.medextest.core.common

fun UiMessage.text(): String = when (this) {
    UiMessage.ComingSoon -> "Раздел появится в следующих версиях"
    UiMessage.NetworkError -> "Не удалось выполнить запрос. Попробуйте ещё раз"
    UiMessage.CheckInSaved -> "Отметка сохранена"
    UiMessage.CopiedToClipboard -> "Скопировано"
}
