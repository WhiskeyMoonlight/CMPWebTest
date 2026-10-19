package org.dimas.medextest.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

@Composable
internal fun rememberAppNavEntries(navigator: AppNavigator): List<NavEntry<NavKey>> {
    val decorators = listOf(
        rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
        rememberViewModelStoreNavEntryDecorator(),
    )
    val entryProvider = remember(navigator) { appEntryProvider(navigator) }
    val entriesByDestination = TopLevelDestination.entries.associateWith { destination ->
        key(destination) {
            rememberDecoratedNavEntries(
                backStack = navigator.stackOf(destination),
                entryDecorators = decorators,
                entryProvider = entryProvider,
            )
        }
    }
    return buildList {
        if (navigator.currentDestination != TopLevelDestination.Home) {
            addAll(entriesByDestination.getValue(TopLevelDestination.Home))
        }
        addAll(entriesByDestination.getValue(navigator.currentDestination))
    }
}
