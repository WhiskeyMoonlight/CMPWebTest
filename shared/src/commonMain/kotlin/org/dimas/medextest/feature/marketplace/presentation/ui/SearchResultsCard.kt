package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.EmptyBlock
import org.dimas.medextest.core.designsystem.component.MenuRow
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.marketplace.domain.model.SearchResult
import org.dimas.medextest.feature.marketplace.presentation.mvi.SearchState
import org.dimas.medextest.feature.marketplace.presentation.util.subtitle

@Composable
internal fun SearchResultsCard(
    search: SearchState,
    onResultClick: (SearchResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    FullBleedCard(modifier) {
        when (search) {
            SearchState.Idle -> Unit
            SearchState.Searching -> SearchProgress()
            SearchState.Failed -> EmptyBlock("Поиск не удался. Попробуйте ещё раз")
            is SearchState.Results -> SearchResults(search, onResultClick)
        }
    }
}

@Composable
private fun SearchProgress() {
    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp, color = MedExColors.Teal)
        Spacer(Modifier.width(12.dp))
        Text("Ищем…", style = MaterialTheme.typography.bodyMedium, color = MedExColors.InkSecondary)
    }
}

@Composable
private fun SearchResults(results: SearchState.Results, onResultClick: (SearchResult) -> Unit) {
    if (results.items.isEmpty()) {
        EmptyBlock("По запросу «${results.query}» ничего не найдено")
        return
    }
    results.items.forEachIndexed { index, result ->
        MenuRow(
            title = result.title,
            subtitle = result.subtitle(),
            onClick = { onResultClick(result) },
            showDivider = index != results.items.lastIndex,
        )
    }
}
