package org.dimas.medextest.feature.medcard.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.feature.medcard.presentation.ui.MedCardRoute

fun EntryProviderScope<NavKey>.medCardEntries() {
    entry<MedCardNavKey> {
        MedCardRoute()
    }
}
