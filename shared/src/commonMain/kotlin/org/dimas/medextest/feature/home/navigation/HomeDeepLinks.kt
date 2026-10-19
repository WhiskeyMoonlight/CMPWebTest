package org.dimas.medextest.feature.home.navigation

import org.dimas.medextest.core.navigation.DeepLink
import org.dimas.medextest.core.navigation.deepLink

val homeDeepLinks: List<DeepLink<*>> = listOf(deepLink<HomeNavKey>("home"))
