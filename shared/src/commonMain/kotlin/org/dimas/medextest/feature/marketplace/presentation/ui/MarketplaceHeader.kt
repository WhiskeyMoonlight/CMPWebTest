package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.contentMaxWidth
import org.dimas.medextest.core.designsystem.component.CenteredTitleBar
import org.dimas.medextest.core.designsystem.component.SearchField

@Composable
internal fun MarketplaceHeader(
    query: TextFieldState,
    onSearchSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val widthClass = LocalWindowWidthClass.current
    Column(
        modifier = modifier.fillMaxWidth().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(Modifier.widthIn(max = widthClass.contentMaxWidth).fillMaxWidth()) {
            CenteredTitleBar(title = "Витрина")
            SearchField(
                state = query,
                placeholder = "Найти услугу",
                onSearchClick = onSearchSubmit,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 12.dp),
            )
        }
    }
}
