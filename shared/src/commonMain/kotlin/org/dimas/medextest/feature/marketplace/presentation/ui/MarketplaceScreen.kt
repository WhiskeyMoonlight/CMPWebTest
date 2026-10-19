package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.marketplace.presentation.mvi.MarketplaceActions
import org.dimas.medextest.feature.marketplace.presentation.mvi.MarketplaceState
import org.dimas.medextest.feature.marketplace.presentation.mvi.SearchState

private const val SEARCH_ITEM_KEY = "search"

@Composable
fun MarketplaceScreen(
    state: MarketplaceState,
    query: TextFieldState,
    actions: MarketplaceActions,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().background(MedExColors.Background)) {
        Column(Modifier.fillMaxSize()) {
            MarketplaceHeader(query = query, onSearchSubmit = actions::onSearchSubmit)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (state.search != SearchState.Idle) {
                    item(key = SEARCH_ITEM_KEY, contentType = SEARCH_ITEM_KEY) {
                        SearchResultsCard(search = state.search, onResultClick = actions::onSearchResultClick)
                    }
                }
                items(items = state.sections, key = { it.id.name }, contentType = { it.id.name }) { section ->
                    MarketplaceSectionCard(section = section, actions = actions)
                }
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}
