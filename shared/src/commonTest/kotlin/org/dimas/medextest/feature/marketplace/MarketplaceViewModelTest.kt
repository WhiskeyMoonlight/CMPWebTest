package org.dimas.medextest.feature.marketplace

import kotlinx.coroutines.test.runTest
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId
import org.dimas.medextest.feature.marketplace.domain.usecase.LoadMarketplaceSectionUseCase
import org.dimas.medextest.feature.marketplace.domain.usecase.SearchServicesUseCase
import org.dimas.medextest.feature.marketplace.presentation.MarketplaceViewModel
import org.dimas.medextest.feature.marketplace.presentation.mvi.MarketplaceState
import org.dimas.medextest.testutil.FakeMarketplaceRepository
import org.dimas.medextest.testutil.MainDispatcherTest
import org.dimas.medextest.testutil.awaitStateMatching
import org.orbitmvi.orbit.test.testWithInternalState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class MarketplaceViewModelTest : MainDispatcherTest() {
    private fun MarketplaceState.load(id: MarketplaceSectionId) = sections.single { it.id == id }.load

    @Test
    fun failed_section_does_not_block_others_and_retry_reloads_only_it() = runTest {
        val repository = FakeMarketplaceRepository(failOnce = setOf(MarketplaceSectionId.Doctors))
        val viewModel = MarketplaceViewModel(LoadMarketplaceSectionUseCase(repository), SearchServicesUseCase(repository))

        viewModel.testWithInternalState(this) {
            runOnCreate()
            val settled = awaitStateMatching { state -> state.sections.none { it.load == LoadState.Loading } }
            assertIs<LoadState.Error>(settled.load(MarketplaceSectionId.Doctors))
            MarketplaceSectionId.entries
                .filter { it != MarketplaceSectionId.Doctors }
                .forEach { assertIs<LoadState.Content<*>>(settled.load(it), "section $it") }

            containerHost.onRetrySection(MarketplaceSectionId.Doctors)
            awaitStateMatching { it.load(MarketplaceSectionId.Doctors) is LoadState.Content }

            assertEquals(2, repository.calls[MarketplaceSectionId.Doctors])
            assertEquals(1, repository.calls[MarketplaceSectionId.Promotions])
            cancelAndIgnoreRemainingItems()
        }
    }
}
