package org.dimas.medextest.feature.marketplace.navigation

import org.dimas.medextest.core.navigation.DeepLink
import org.dimas.medextest.core.navigation.deepLink

val marketplaceDeepLinks: List<DeepLink<*>> = listOf(
    deepLink<MarketplaceNavKey>("marketplace"),
    deepLink<MarketplaceSectionNavKey>(
        path = "marketplace/section/{sectionSlug}",
        backStack = { listOf(MarketplaceNavKey, it) },
        pathOf = { "marketplace/section/${it.sectionSlug}" },
    ),
)
