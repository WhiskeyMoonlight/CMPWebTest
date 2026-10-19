package org.dimas.medextest.feature.more.devsettings.navigation

import org.dimas.medextest.core.navigation.DeepLink
import org.dimas.medextest.core.navigation.deepLink
import org.dimas.medextest.feature.more.navigation.MoreNavKey

val devSettingsDeepLinks: List<DeepLink<*>> = listOf(
    deepLink<DevSettingsNavKey>("more/dev", backStack = { listOf(MoreNavKey, it) }),
)
