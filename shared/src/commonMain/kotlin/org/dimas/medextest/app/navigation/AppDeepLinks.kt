package org.dimas.medextest.app.navigation

import org.dimas.medextest.core.navigation.DeepLinks
import org.dimas.medextest.feature.chat.navigation.chatDeepLinks
import org.dimas.medextest.feature.home.navigation.homeDeepLinks
import org.dimas.medextest.feature.marketplace.navigation.marketplaceDeepLinks
import org.dimas.medextest.feature.medcard.navigation.medCardDeepLinks
import org.dimas.medextest.feature.more.capabilities.navigation.capabilitiesDeepLinks
import org.dimas.medextest.feature.more.devsettings.navigation.devSettingsDeepLinks
import org.dimas.medextest.feature.more.navigation.moreDeepLinks

internal val appDeepLinks = DeepLinks(
    homeDeepLinks + marketplaceDeepLinks + chatDeepLinks + medCardDeepLinks +
        moreDeepLinks + capabilitiesDeepLinks + devSettingsDeepLinks,
)
