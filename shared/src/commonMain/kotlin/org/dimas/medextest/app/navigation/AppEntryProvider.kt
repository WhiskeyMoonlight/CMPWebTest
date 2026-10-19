package org.dimas.medextest.app.navigation

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import org.dimas.medextest.feature.chat.navigation.chatEntries
import org.dimas.medextest.feature.home.navigation.homeEntries
import org.dimas.medextest.feature.marketplace.navigation.marketplaceEntries
import org.dimas.medextest.feature.medcard.navigation.medCardEntries
import org.dimas.medextest.feature.more.capabilities.navigation.capabilitiesEntries
import org.dimas.medextest.feature.more.devsettings.navigation.devSettingsEntries
import org.dimas.medextest.feature.more.navigation.moreEntries

internal fun appEntryProvider(navigator: AppNavigator): (NavKey) -> NavEntry<NavKey> = entryProvider {
    homeEntries(onOpenMarketplace = { navigator.selectDestination(TopLevelDestination.Marketplace) })
    marketplaceEntries(navigator)
    chatEntries(navigator)
    medCardEntries()
    moreEntries(navigator)
    capabilitiesEntries(navigator)
    devSettingsEntries(navigator)
}
