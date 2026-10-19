package org.dimas.medextest.app.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.core.navigation.Navigator

@Stable
class AppNavigator(initialDestination: TopLevelDestination = TopLevelDestination.Home) : Navigator {
    private val stacks: Map<TopLevelDestination, SnapshotStateList<NavKey>> =
        TopLevelDestination.entries.associateWith { destination -> mutableStateListOf(destination.root) }

    var currentDestination: TopLevelDestination by mutableStateOf(initialDestination)
        private set

    val currentKey: NavKey get() = stacks.getValue(currentDestination).last()

    fun stackOf(destination: TopLevelDestination): List<NavKey> = stacks.getValue(destination)

    override fun navigate(key: NavKey) {
        stacks.getValue(currentDestination).add(key)
    }

    override fun back() {
        val stack = stacks.getValue(currentDestination)
        when {
            stack.size > 1 -> stack.removeAt(stack.lastIndex)
            currentDestination != TopLevelDestination.Home -> currentDestination = TopLevelDestination.Home
        }
    }

    fun selectDestination(destination: TopLevelDestination) {
        if (destination == currentDestination) popToRoot(destination) else currentDestination = destination
    }

    fun replaceStack(stack: List<NavKey>) {
        val destination = TopLevelDestination.entries.firstOrNull { it.root == stack.firstOrNull() }
        val target = destination ?: TopLevelDestination.Home
        val keys = if (destination != null) stack else listOf(target.root) + stack
        Snapshot.withMutableSnapshot {
            val targetStack = stacks.getValue(target)
            targetStack.clear()
            targetStack.addAll(keys)
            currentDestination = target
        }
    }

    private fun popToRoot(destination: TopLevelDestination) {
        val stack = stacks.getValue(destination)
        while (stack.size > 1) stack.removeAt(stack.lastIndex)
    }
}
