package org.dimas.medextest.app.navigation

import androidx.navigation3.runtime.NavKey
import org.dimas.medextest.feature.chat.navigation.ChatNavKey
import org.dimas.medextest.feature.home.navigation.HomeNavKey
import org.dimas.medextest.feature.marketplace.navigation.MarketplaceNavKey
import org.dimas.medextest.feature.marketplace.navigation.MarketplaceSectionNavKey
import org.dimas.medextest.feature.medcard.navigation.MedCardNavKey
import org.dimas.medextest.feature.more.capabilities.navigation.CapabilitiesNavKey
import org.dimas.medextest.feature.more.devsettings.navigation.DevSettingsNavKey
import org.dimas.medextest.feature.more.navigation.MoreNavKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AppDeepLinksTest {
    private val backStacksByKey: Map<NavKey, List<NavKey>> = mapOf(
        HomeNavKey to listOf(HomeNavKey),
        MarketplaceNavKey to listOf(MarketplaceNavKey),
        MarketplaceSectionNavKey("lab-tests") to listOf(MarketplaceNavKey, MarketplaceSectionNavKey("lab-tests")),
        MedCardNavKey to listOf(MedCardNavKey),
        MoreNavKey to listOf(MoreNavKey),
        CapabilitiesNavKey to listOf(MoreNavKey, CapabilitiesNavKey),
        DevSettingsNavKey to listOf(MoreNavKey, DevSettingsNavKey),
        ChatNavKey to listOf(ChatNavKey),
    )

    @Test
    fun path_of_every_key_parses_back_to_its_back_stack() {
        backStacksByKey.forEach { (key, backStack) ->
            val path = assertNotNull(appDeepLinks.pathOf(key), "no deep link for $key")
            assertEquals(backStack, appDeepLinks.parse(path), "round trip of '$path'")
        }
    }

    @Test
    fun paths_are_human_readable() {
        assertEquals("more/capabilities", appDeepLinks.pathOf(CapabilitiesNavKey))
        assertEquals("marketplace/section/promotions", appDeepLinks.pathOf(MarketplaceSectionNavKey("promotions")))
    }

    @Test
    fun unknown_or_malformed_paths_are_ignored() {
        listOf("", "/", "unknown", "more/unknown", "marketplace/section", "home/extra", "chat/1").forEach { path ->
            assertNull(appDeepLinks.parse(path), "expected null for '$path'")
        }
    }

    @Test
    fun surrounding_slashes_are_tolerated() {
        assertEquals(listOf(MoreNavKey), appDeepLinks.parse("/more/"))
    }

    @Test
    fun every_destination_root_is_linkable() {
        TopLevelDestination.entries.forEach { destination ->
            assertNotNull(appDeepLinks.pathOf(destination.root), "no deep link for $destination")
        }
    }
}
