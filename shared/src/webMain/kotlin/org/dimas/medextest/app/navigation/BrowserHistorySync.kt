package org.dimas.medextest.app.navigation

import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.core.navigation.DeepLinks
import web.events.EventHandler
import web.events.addEventListener
import web.events.removeEventListener
import web.history.POP_STATE
import web.history.PopStateEvent
import web.history.history
import web.location.location
import web.window.window
import kotlin.js.toJsString

internal class BrowserHistorySync(
    private val navigator: AppNavigator,
    private val deepLinks: DeepLinks,
) {
    private val pathsByIndex = mutableMapOf<Int, String>()
    private var index = 0
    private var ignoredPopStates = 0
    private val popStateHandler = EventHandler { onPopState() }

    fun start() {
        deepLinks.parse(currentPath())?.let(navigator::replaceStack)
        index = 0
        pathsByIndex.clear()
        deepLinks.pathOf(navigator.currentKey)?.let { path ->
            pathsByIndex[index] = path
            history.replaceState(index.toString().toJsString(), "", hashOf(path))
        }
        window.addEventListener(PopStateEvent.POP_STATE, popStateHandler)
    }

    fun stop() {
        window.removeEventListener(PopStateEvent.POP_STATE, popStateHandler)
    }

    fun onKeyChanged(key: NavKey) {
        val path = deepLinks.pathOf(key) ?: return
        when (path) {
            pathsByIndex[index] -> Unit
            pathsByIndex[index - 1] -> goBackInHistory()
            else -> pushToHistory(path)
        }
    }

    private fun goBackInHistory() {
        index -= 1
        ignoredPopStates += 1
        history.back()
    }

    private fun pushToHistory(path: String) {
        index += 1
        pathsByIndex.keys.removeAll { it >= index }
        pathsByIndex[index] = path
        history.pushState(index.toString().toJsString(), "", hashOf(path))
    }

    private fun onPopState() {
        if (ignoredPopStates > 0) {
            ignoredPopStates -= 1
            return
        }
        index = history.state?.toString()?.toIntOrNull() ?: 0
        val path = currentPath()
        pathsByIndex[index] = path
        navigator.replaceStack(deepLinks.parse(path) ?: listOf(TopLevelDestination.Home.root))
    }

    private fun currentPath(): String = location.hash.removePrefix("#").trim('/')

    private fun hashOf(path: String): String = "#/$path"
}
