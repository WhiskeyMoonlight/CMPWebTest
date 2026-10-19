package org.dimas.medextest.feature.more.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.core.navigation.Navigator
import org.dimas.medextest.feature.more.capabilities.navigation.CapabilitiesNavKey
import org.dimas.medextest.feature.more.devsettings.navigation.DevSettingsNavKey
import org.dimas.medextest.feature.more.presentation.ui.MoreRoute

fun EntryProviderScope<NavKey>.moreEntries(navigator: Navigator) {
    entry<MoreNavKey> {
        MoreRoute(
            onOpenCapabilities = { navigator.navigate(CapabilitiesNavKey) },
            onOpenDevSettings = { navigator.navigate(DevSettingsNavKey) },
        )
    }
}
