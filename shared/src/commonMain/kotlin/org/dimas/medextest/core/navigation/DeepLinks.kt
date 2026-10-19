package org.dimas.medextest.core.navigation

import androidx.navigation3.runtime.NavKey

class DeepLinks(private val links: List<DeepLink<*>>) {
    fun parse(path: String): List<NavKey>? = links.firstNotNullOfOrNull { link -> link.match(path) }

    fun pathOf(key: NavKey): String? = links.firstNotNullOfOrNull { link -> link.pathOf(key) }
}
