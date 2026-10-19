package org.dimas.medextest.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.ui.NavDisplay
import org.dimas.medextest.app.navigation.AppNavigator
import org.dimas.medextest.app.navigation.NavigatorHolder
import org.dimas.medextest.app.navigation.TopLevelDestination
import org.dimas.medextest.app.navigation.rememberAppNavEntries
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.isWide
import org.dimas.medextest.core.designsystem.component.MedExBottomBar
import org.dimas.medextest.core.designsystem.component.MedExNavigationRail
import org.dimas.medextest.core.designsystem.component.NavigationItem
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.chat.navigation.ChatNavKey

@Composable
internal fun MainScaffold(navigatorBinding: @Composable (AppNavigator) -> Unit) {
    val navigator = viewModel { NavigatorHolder() }.navigator
    navigatorBinding(navigator)

    val entries = rememberAppNavEntries(navigator)
    val items = remember { TopLevelDestination.entries.map { NavigationItem(it.label, it.icon) } }
    val isWide = LocalWindowWidthClass.current.isWide
    val showNavigation = navigator.currentKey != ChatNavKey
    val selectedIndex = navigator.currentDestination.ordinal
    val onItemClick: (Int) -> Unit = { index -> navigator.selectDestination(TopLevelDestination.entries[index]) }
    val onAssistantClick: () -> Unit = { navigator.navigate(ChatNavKey) }

    Surface(modifier = Modifier.fillMaxSize(), color = MedExColors.Background, contentColor = MedExColors.Ink) {
        Row(Modifier.fillMaxSize()) {
            if (isWide && showNavigation) {
                MedExNavigationRail(
                    items = items,
                    selectedIndex = selectedIndex,
                    onItemClick = onItemClick,
                    onAssistantClick = onAssistantClick,
                )
            }
            Column(Modifier.weight(1f).fillMaxHeight()) {
                NavDisplay(
                    entries = entries,
                    onBack = navigator::back,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                )
                if (!isWide && showNavigation) {
                    MedExBottomBar(
                        items = items,
                        selectedIndex = selectedIndex,
                        onItemClick = onItemClick,
                        onAssistantClick = onAssistantClick,
                    )
                }
            }
        }
    }
}
