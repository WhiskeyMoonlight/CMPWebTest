package org.dimas.medextest.feature.more.devsettings.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.core.navigation.Navigator
import org.dimas.medextest.feature.more.devsettings.presentation.ui.DevSettingsRoute

fun EntryProviderScope<NavKey>.devSettingsEntries(navigator: Navigator) {
    entry<DevSettingsNavKey> {
        DevSettingsRoute(onBack = navigator::back)
    }
}
