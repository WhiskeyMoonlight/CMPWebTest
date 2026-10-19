package org.dimas.medextest.feature.chat.navigation

import org.dimas.medextest.core.navigation.DeepLink
import org.dimas.medextest.core.navigation.deepLink

val chatDeepLinks: List<DeepLink<*>> = listOf(
    deepLink<ChatNavKey>("chat"),
)
