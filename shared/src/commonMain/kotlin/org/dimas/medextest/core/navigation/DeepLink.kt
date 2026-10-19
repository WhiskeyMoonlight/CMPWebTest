package org.dimas.medextest.core.navigation

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.deeplink.DeepLinkRequest
import androidx.navigation3.runtime.deeplink.DeepLinkUri
import androidx.navigation3.runtime.deeplink.UriDeepLinkMatcher
import androidx.navigation3.runtime.deeplink.withBackStack
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

class DeepLink<K : NavKey>(
    val path: String,
    private val keyClass: KClass<K>,
    serializer: KSerializer<K>,
    private val backStack: (K) -> List<NavKey>,
    pathOf: (K) -> String,
) {
    private val keyPath = pathOf

    private val matcher by lazy {
        UriDeepLinkMatcher(DeepLinkUri(uriOf(path)), serializer)
            .withBackStack { result -> backStack(result.key) }
    }

    fun match(path: String): List<NavKey>? =
        matcher.match(DeepLinkRequest(DeepLinkUri(uriOf(path.trim('/')))))?.backStack

    fun pathOf(key: NavKey): String? = keyClass.safeCast(key)?.let(keyPath)

    private fun uriOf(path: String): String = "$BASE_URI$path"

    private companion object {
        const val BASE_URI = "https://medex.app/"
    }
}
