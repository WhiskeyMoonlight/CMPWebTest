package org.dimas.medextest.app.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.core.designsystem.icon.MedExIcons
import org.dimas.medextest.feature.home.navigation.HomeNavKey
import org.dimas.medextest.feature.marketplace.navigation.MarketplaceNavKey
import org.dimas.medextest.feature.medcard.navigation.MedCardNavKey
import org.dimas.medextest.feature.more.navigation.MoreNavKey

enum class TopLevelDestination(val root: NavKey, val label: String, val icon: ImageVector) {
    Home(HomeNavKey, "Главная", MedExIcons.TabHome),
    Marketplace(MarketplaceNavKey, "Витрина", MedExIcons.TabShowcase),
    MedCard(MedCardNavKey, "Медкарта", MedExIcons.TabMedCard),
    More(MoreNavKey, "Ещё", MedExIcons.TabMore),
}
