package org.dimas.medextest.feature.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.feature.home.presentation.ui.HomeRoute

fun EntryProviderScope<NavKey>.homeEntries(onOpenMarketplace: () -> Unit) {
    entry<HomeNavKey> {
        HomeRoute(onOpenSearch = onOpenMarketplace)
    }
}
