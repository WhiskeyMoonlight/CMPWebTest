package org.dimas.medextest.feature.marketplace.presentation.mvi

import org.dimas.medextest.feature.marketplace.domain.model.SearchResult

sealed interface SearchState {
    data object Idle : SearchState
    data object Searching : SearchState
    data class Results(val query: String, val items: List<SearchResult>) : SearchState
    data object Failed : SearchState
}
