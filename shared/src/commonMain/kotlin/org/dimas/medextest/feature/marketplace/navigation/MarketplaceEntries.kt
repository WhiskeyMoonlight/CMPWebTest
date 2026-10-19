package org.dimas.medextest.feature.marketplace.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.core.navigation.Navigator
import org.dimas.medextest.feature.marketplace.presentation.ui.MarketplaceRoute
import org.dimas.medextest.feature.marketplace.presentation.ui.MarketplaceSectionRoute

fun EntryProviderScope<NavKey>.marketplaceEntries(navigator: Navigator) {
    entry<MarketplaceNavKey> {
        MarketplaceRoute(onOpenSection = { section -> navigator.navigate(MarketplaceSectionNavKey(section.slug)) })
    }

    entry<MarketplaceSectionNavKey> { key ->
        MarketplaceSectionRoute(sectionSlug = key.sectionSlug, onBack = navigator::back)
    }
}
