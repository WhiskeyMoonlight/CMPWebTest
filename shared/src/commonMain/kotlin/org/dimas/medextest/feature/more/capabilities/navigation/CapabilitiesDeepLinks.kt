package org.dimas.medextest.feature.more.capabilities.navigation

import org.dimas.medextest.core.navigation.DeepLink
import org.dimas.medextest.core.navigation.deepLink
import org.dimas.medextest.feature.more.navigation.MoreNavKey

val capabilitiesDeepLinks: List<DeepLink<*>> = listOf(
    deepLink<CapabilitiesNavKey>("more/capabilities", backStack = { listOf(MoreNavKey, it) }),
)
