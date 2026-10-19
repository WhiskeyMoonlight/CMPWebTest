package org.dimas.medextest.feature.chat.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.core.navigation.Navigator
import org.dimas.medextest.feature.chat.presentation.ui.ChatRoute

fun EntryProviderScope<NavKey>.chatEntries(navigator: Navigator) {
    entry<ChatNavKey> {
        ChatRoute(onBack = navigator::back)
    }
}
