package org.dimas.medextest.feature.more.loadlab.data

import org.dimas.medextest.compute.lab.LabPageGenerator
import org.dimas.medextest.core.mock.MockBackend
import org.dimas.medextest.feature.more.loadlab.domain.repository.LabRepository
import kotlin.time.Duration.Companion.milliseconds

class MockLabRepository(private val backend: MockBackend) : LabRepository {
    private val responses: List<String> by lazy {
        List(DISTINCT_PAGES) { page -> LabPageGenerator.json(page = page, pageSize = PAGE_SIZE, seed = SEED) }
    }

    override val pageSize: Int = PAGE_SIZE

    override suspend fun fetchPage(page: Int): String =
        backend.call("GET /lab/results?page=$page", latency = PAGE_LATENCY) { responses[page % DISTINCT_PAGES] }

    private companion object {
        const val PAGE_SIZE = 1_500
        const val DISTINCT_PAGES = 8
        const val SEED = 2026
        val PAGE_LATENCY = 120.milliseconds
    }
}
