package org.dimas.medextest.feature.home.presentation.util

import org.dimas.medextest.core.designsystem.icon.MedExIcons
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.home.domain.model.QuickAction
import org.dimas.medextest.feature.home.presentation.model.QuickActionUi

internal fun QuickAction.ui(): QuickActionUi = when (this) {
    QuickAction.BookVisit -> QuickActionUi("Запись\nв клинику", MedExIcons.CalendarPlus, MedExColors.TealLight, MedExColors.CyanSoft)
    QuickAction.HomeVisit -> QuickActionUi("Вызвать\nна дом", MedExIcons.House, MedExColors.Pink, MedExColors.PinkSoft)
    QuickAction.CallClinic -> QuickActionUi("Позвонить\nв клинику", MedExIcons.PhoneIncoming, MedExColors.Green, MedExColors.GreenSoft)
}
