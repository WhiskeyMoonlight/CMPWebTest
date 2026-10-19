package org.dimas.medextest.feature.more.presentation.model

enum class MoreMenuItem(val title: String) {
    ProfileSettings("Настройки профиля"),
    Insurance("ДМС и гарантийные письма"),
    PromoCode("Промокод"),
    Cards("Привязанные карты"),
    Security("Безопасность"),
    Notifications("Уведомления"),
    Clinics("Доступные клиники"),
    AboutClinic("О клинике"),
    AboutApp("О приложении"),
    ;

    companion object {
        val accountItems: List<MoreMenuItem> = listOf(
            ProfileSettings, Insurance, PromoCode,
            Cards, Security, Notifications,
        )
        val clinicItems: List<MoreMenuItem> = listOf(Clinics, AboutClinic, AboutApp)
    }
}
