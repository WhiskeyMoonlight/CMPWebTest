package org.dimas.medextest.feature.marketplace.data

import org.dimas.medextest.feature.marketplace.domain.model.DoctorSpecialty
import org.dimas.medextest.feature.marketplace.domain.model.Palette
import org.dimas.medextest.feature.marketplace.domain.model.Promotion
import org.dimas.medextest.feature.marketplace.domain.model.ServiceCategory
import org.dimas.medextest.feature.marketplace.domain.model.ServiceOffer
import org.dimas.medextest.feature.marketplace.domain.model.ServicePackage

internal object MarketplaceCatalog {
    val promotions = listOf(
        Promotion("promo-birthday", "Подарок на День Рождения", "1000 ₽", Palette.Peach),
        Promotion("promo-ivf", "Скидка 10% на базовую программу ЭКО", "до 30 июня", Palette.Pink),
        Promotion("promo-checkup", "Чек-ап за один день", "−15%", Palette.Mint),
        Promotion("promo-pediatrics", "Консультация педиатра онлайн", "бесплатно", Palette.Sky),
        Promotion("promo-family", "Семейная программа наблюдения", "−20%", Palette.Lavender),
    )

    val doctors = listOf(
        DoctorSpecialty("doc-neuro", "Невролог", 1450, listOf(1, 2, 3)),
        DoctorSpecialty("doc-therapist", "Терапевт", 1450, listOf(4, 0, 2)),
        DoctorSpecialty("doc-urologist", "Уролог", 1650, listOf(2, 1)),
        DoctorSpecialty("doc-cardio", "Кардиолог", 1900, listOf(3, 4, 1)),
        DoctorSpecialty("doc-gyneco", "Гинеколог", 1750, listOf(0, 3)),
        DoctorSpecialty("doc-endo", "Эндокринолог", 1850, listOf(1, 4)),
        DoctorSpecialty("doc-derma", "Дерматолог", 1550, listOf(2, 0, 4)),
        DoctorSpecialty("doc-ophthalmo", "Офтальмолог", 1600, listOf(3, 2)),
    )

    val labTests = listOf(
        ServiceOffer("lab-complex", "Комплексы анализов", 1450),
        ServiceOffer("lab-pregnancy", "Беременность", 1450),
        ServiceOffer("lab-hormones", "Гормоны", 1450),
        ServiceOffer("lab-vitamins", "Витамины и микроэлементы", 990),
        ServiceOffer("lab-allergy", "Аллергопанель", 2400),
        ServiceOffer("lab-biochem", "Биохимия крови", 1200),
    )

    val packages = listOf(
        ServicePackage("pkg-care", "Забота о здоровье", 1450, 3450, Palette.Sky),
        ServicePackage("pkg-gut", "Спокойный ЖКТ", 1450, null, Palette.Peach),
        ServicePackage("pkg-heart", "Счастливое сердце", 4900, 6200, Palette.Pink),
        ServicePackage("pkg-women", "Женское здоровье", 5900, null, Palette.Lavender),
        ServicePackage("pkg-40", "Чек-ап 40+", 8900, 10_500, Palette.Mint),
    )

    val examinations = listOf(
        ServiceOffer("exam-us-abdomen", "УЗИ органов брюшной полости", 1450),
        ServiceOffer("exam-mri-spine", "МРТ пояснично-крестцового отдела позвоночника", 1450),
        ServiceOffer("exam-mri-brain", "МРТ головного мозга", 4500),
        ServiceOffer("exam-ct-chest", "КТ грудной клетки", 3900),
        ServiceOffer("exam-ecg", "ЭКГ с расшифровкой", 900),
        ServiceOffer("exam-holter", "Холтеровское мониторирование", 2900),
    )

    val services = listOf(
        "Аллергология", "Гастро-энтерология", "Гинекология", "Липидология", "Дерматология и венерология",
        "Маммология", "Мануальная терапия", "Неврология", "Онкология", "Оториноларингология",
        "Офтальмология", "Педиатрия", "Психотерапия", "Пульмонология", "Ревматология",
        "Стоматология", "Травматология и ортопедия", "Урология", "Эндокринология",
    ).mapIndexed { index, title -> ServiceCategory("svc-$index", title) }
}
