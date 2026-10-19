package org.dimas.medextest.feature.marketplace

import kotlinx.coroutines.test.runTest
import org.dimas.medextest.feature.marketplace.data.MockMarketplaceRepository
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId
import org.dimas.medextest.feature.marketplace.domain.usecase.SearchServicesUseCase
import org.dimas.medextest.testutil.instantBackend
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MarketplaceSearchTest {
    private val search = SearchServicesUseCase(MockMarketplaceRepository(instantBackend()))

    @Test
    fun short_queries_return_nothing() = runTest {
        assertTrue(search(" м ").isEmpty())
    }

    @Test
    fun search_is_case_insensitive_across_sections() = runTest {
        val results = search("мрт")
        assertEquals(2, results.size)
        assertTrue(results.all { it.section == MarketplaceSectionId.Examinations })
    }
}
