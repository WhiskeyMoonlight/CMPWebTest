package org.dimas.medextest.feature.more.loadlab.domain.model

enum class LoadScenario(val title: String, val description: String) {
    LabImport("Импорт анализов", "Страницы JSON по сети → разбор → статистика по показателям"),
    AttachmentHash("SHA-256 вложений", "Хэш нескольких файлов перед загрузкой"),
    ScanFilter("Фильтр снимка", "Размытие, гистограмма и порог Оцу для скана 24 Мп"),
    RequestStorm("Шторм запросов", "Тысячи корутин, семафор, ретраи и общий счётчик"),
}
