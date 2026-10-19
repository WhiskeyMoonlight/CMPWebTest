package org.dimas.medextest.feature.medcard.navigation

import org.dimas.medextest.core.navigation.DeepLink
import org.dimas.medextest.core.navigation.deepLink

val medCardDeepLinks: List<DeepLink<*>> = listOf(
    deepLink<MedCardNavKey>("medcard"),
)
