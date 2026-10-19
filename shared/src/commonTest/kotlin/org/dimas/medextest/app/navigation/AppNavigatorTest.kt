package org.dimas.medextest.app.navigation

import org.dimas.medextest.feature.chat.navigation.ChatNavKey
import org.dimas.medextest.feature.home.navigation.HomeNavKey
import org.dimas.medextest.feature.marketplace.navigation.MarketplaceNavKey
import org.dimas.medextest.feature.marketplace.navigation.MarketplaceSectionNavKey
import org.dimas.medextest.feature.more.capabilities.navigation.CapabilitiesNavKey
import org.dimas.medextest.feature.more.devsettings.navigation.DevSettingsNavKey
import org.dimas.medextest.feature.more.navigation.MoreNavKey
import kotlin.test.Test
import kotlin.test.assertEquals

class AppNavigatorTest {
    @Test
    fun navigate_pushes_onto_the_current_destination_and_back_pops_it() {
        val navigator = AppNavigator()
        navigator.selectDestination(TopLevelDestination.More)
        navigator.navigate(CapabilitiesNavKey)

        assertEquals(CapabilitiesNavKey, navigator.currentKey)
        navigator.back()
        assertEquals(MoreNavKey, navigator.currentKey)
    }

    @Test
    fun back_from_another_destination_root_returns_to_home() {
        val navigator = AppNavigator()
        navigator.selectDestination(TopLevelDestination.Marketplace)

        navigator.back()

        assertEquals(TopLevelDestination.Home, navigator.currentDestination)
        assertEquals(HomeNavKey, navigator.currentKey)
    }

    @Test
    fun back_on_the_home_root_changes_nothing() {
        val navigator = AppNavigator()

        navigator.back()

        assertEquals(TopLevelDestination.Home, navigator.currentDestination)
        assertEquals(listOf(HomeNavKey), navigator.stackOf(TopLevelDestination.Home).toList())
    }

    @Test
    fun destinations_keep_their_own_stacks() {
        val navigator = AppNavigator()
        navigator.selectDestination(TopLevelDestination.More)
        navigator.navigate(DevSettingsNavKey)
        navigator.selectDestination(TopLevelDestination.Marketplace)
        navigator.selectDestination(TopLevelDestination.More)

        assertEquals(listOf(MoreNavKey, DevSettingsNavKey), navigator.stackOf(TopLevelDestination.More).toList())
    }

    @Test
    fun selecting_the_current_destination_again_pops_it_to_root() {
        val navigator = AppNavigator()
        navigator.selectDestination(TopLevelDestination.More)
        navigator.navigate(CapabilitiesNavKey)
        navigator.selectDestination(TopLevelDestination.More)

        assertEquals(listOf(MoreNavKey), navigator.stackOf(TopLevelDestination.More).toList())
    }

    @Test
    fun replacing_with_a_stack_that_starts_with_a_root_switches_to_that_destination() {
        val navigator = AppNavigator()

        navigator.replaceStack(listOf(MarketplaceNavKey, MarketplaceSectionNavKey("lab-tests")))

        assertEquals(TopLevelDestination.Marketplace, navigator.currentDestination)
        assertEquals(MarketplaceSectionNavKey("lab-tests"), navigator.currentKey)
    }

    @Test
    fun replacing_with_a_stack_without_a_root_puts_it_on_top_of_home() {
        val navigator = AppNavigator()
        navigator.selectDestination(TopLevelDestination.More)

        navigator.replaceStack(listOf(ChatNavKey))

        assertEquals(TopLevelDestination.Home, navigator.currentDestination)
        assertEquals(listOf(HomeNavKey, ChatNavKey), navigator.stackOf(TopLevelDestination.Home).toList())
    }
}
