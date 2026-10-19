package org.dimas.medextest.feature.more.capabilities.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.core.navigation.Navigator
import org.dimas.medextest.feature.more.capabilities.presentation.ui.CapabilitiesRoute

fun EntryProviderScope<NavKey>.capabilitiesEntries(navigator: Navigator) {
    entry<CapabilitiesNavKey> {
        CapabilitiesRoute(onBack = navigator::back)
    }
}
