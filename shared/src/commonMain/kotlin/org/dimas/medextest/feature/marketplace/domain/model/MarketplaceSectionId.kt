package org.dimas.medextest.feature.marketplace.domain.model

enum class MarketplaceSectionId(val slug: String, val title: String) {
    Promotions("promotions", "Акции"),
    Doctors("doctors", "Записаться к врачу"),
    LabTests("lab-tests", "Анализы"),
    Packages("packages", "Пакеты услуг"),
    Examinations("examinations", "Обследования"),
    Services("services", "Услуги");

    companion object {
        fun fromSlug(slug: String): MarketplaceSectionId? = entries.firstOrNull { it.slug == slug }
    }
}
