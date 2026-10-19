package org.dimas.medextest.feature.more.navigation

import org.dimas.medextest.core.navigation.DeepLink
import org.dimas.medextest.core.navigation.deepLink

val moreDeepLinks: List<DeepLink<*>> = listOf(
    deepLink<MoreNavKey>("more"),
)
