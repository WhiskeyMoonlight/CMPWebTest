package org.dimas.medextest.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.serializer

inline fun <reified K : NavKey> deepLink(
    path: String,
    noinline backStack: (K) -> List<NavKey> = { listOf(it) },
    noinline pathOf: (K) -> String = { path },
): DeepLink<K> = DeepLink(
    path = path,
    keyClass = K::class,
    serializer = serializer<K>(),
    backStack = backStack,
    pathOf = pathOf,
)
