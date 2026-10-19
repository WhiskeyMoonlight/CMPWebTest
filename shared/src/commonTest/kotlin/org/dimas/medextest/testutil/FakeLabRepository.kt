package org.dimas.medextest.testutil

import kotlinx.coroutines.delay
import org.dimas.medextest.compute.lab.LabPageGenerator
import org.dimas.medextest.core.common.NetworkException
import org.dimas.medextest.feature.more.loadlab.domain.repository.LabRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class FakeLabRepository(
    override val pageSize: Int = 20,
    private val latency: Duration = 10.milliseconds,
    private val failuresPerPage: Map<Int, Int> = emptyMap(),
) : LabRepository {
    private val attempts = mutableMapOf<Int, Int>()
    private var inFlight = 0
    var peakInFlight = 0
        private set

    override suspend fun fetchPage(page: Int): String {
        inFlight++
        peakInFlight = maxOf(peakInFlight, inFlight)
        try {
            delay(latency)
            val attempt = (attempts[page] ?: 0) + 1
            attempts[page] = attempt
            if (attempt <= (failuresPerPage[page] ?: 0)) throw NetworkException("GET /lab/results?page=$page")
            return LabPageGenerator.json(page = page, pageSize = pageSize, seed = 1)
        } finally {
            inFlight--
        }
    }
}
