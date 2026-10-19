package org.dimas.medextest.feature.marketplace.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.core.common.UiMessage
import org.dimas.medextest.core.common.runCatchingCancellable
import org.dimas.medextest.core.common.toLoadState
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId
import org.dimas.medextest.feature.marketplace.domain.model.SearchResult
import org.dimas.medextest.feature.marketplace.domain.usecase.LoadMarketplaceSectionUseCase
import org.dimas.medextest.feature.marketplace.domain.usecase.SearchServicesUseCase
import org.dimas.medextest.feature.marketplace.presentation.mvi.MarketplaceActions
import org.dimas.medextest.feature.marketplace.presentation.mvi.MarketplaceSideEffect
import org.dimas.medextest.feature.marketplace.presentation.mvi.MarketplaceState
import org.dimas.medextest.feature.marketplace.presentation.mvi.SearchState
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.orbitContainer
import kotlin.time.Duration.Companion.milliseconds

class MarketplaceViewModel(
    private val loadSection: LoadMarketplaceSectionUseCase,
    private val searchServices: SearchServicesUseCase,
) : ViewModel(), OrbitContainerHost<MarketplaceState, MarketplaceState, MarketplaceSideEffect>, MarketplaceActions {

    val query = TextFieldState()

    private val reloadRequests = MutableSharedFlow<MarketplaceSectionId>(extraBufferCapacity = MarketplaceSectionId.entries.size)
    private val searchSubmits = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    override val container: OrbitContainer<MarketplaceState, MarketplaceState, MarketplaceSideEffect> =
        orbitContainer(initialState = MarketplaceState()) {
            coroutineScope {
                MarketplaceSectionId.entries.forEach { id ->
                    launch {
                        reloadRequests
                            .filter { it == id }
                            .onStart { emit(id) }
                            .collectLatest { loadSectionState(it) }
                    }
                }
                launch { observeSearch() }
            }
        }

    override fun onRetrySection(id: MarketplaceSectionId) {
        reloadRequests.tryEmit(id)
    }

    override fun onSectionMoreClick(id: MarketplaceSectionId) {
        intent { postSideEffect(MarketplaceSideEffect.OpenSection(id)) }
    }

    override fun onItemClick() {
        intent { postSideEffect(MarketplaceSideEffect.ShowMessage(UiMessage.ComingSoon)) }
    }

    override fun onSearchSubmit() {
        searchSubmits.tryEmit(Unit)
    }

    override fun onSearchResultClick(result: SearchResult) {
        intent { postSideEffect(MarketplaceSideEffect.OpenSection(result.section)) }
    }

    private suspend fun Syntax<MarketplaceState, MarketplaceSideEffect>.loadSectionState(id: MarketplaceSectionId) {
        reduce { state.withSection(id, LoadState.Loading) }
        val result = runCatchingCancellable { loadSection(id) }
        reduce { state.withSection(id, result.toLoadState { it.isEmpty }) }
    }

    @OptIn(FlowPreview::class)
    private suspend fun Syntax<MarketplaceState, MarketplaceSideEffect>.observeSearch() {
        val typed = snapshotFlow { query.text.toString() }.debounce(SEARCH_DEBOUNCE)
        val submitted = searchSubmits.map { query.text.toString() }
        merge(typed, submitted).collectLatest { text ->
            val normalized = text.trim()
            if (normalized.length < SearchServicesUseCase.MIN_QUERY_LENGTH) {
                reduce { state.copy(search = SearchState.Idle) }
                return@collectLatest
            }
            reduce { state.copy(search = SearchState.Searching) }
            val result = runCatchingCancellable { searchServices(normalized) }
            reduce {
                state.copy(search = result.fold({ SearchState.Results(normalized, it) }, { SearchState.Failed }))
            }
        }
    }

    private companion object {
        val SEARCH_DEBOUNCE = 300.milliseconds
    }
}
