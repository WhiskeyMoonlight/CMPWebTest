package org.dimas.medextest.feature.more.loadlab.data

import org.dimas.medextest.core.mock.MockBackend
import org.dimas.medextest.feature.more.loadlab.domain.repository.SyncRepository
import kotlin.time.Duration.Companion.milliseconds

class MockSyncRepository(private val backend: MockBackend) : SyncRepository {
    override suspend fun push(recordId: Int): Int =
        backend.call("POST /sync/$recordId", latency = REQUEST_LATENCY) { recordId % REVISIONS + 1 }

    private companion object {
        const val REVISIONS = 97
        val REQUEST_LATENCY = 20.milliseconds
    }
}
