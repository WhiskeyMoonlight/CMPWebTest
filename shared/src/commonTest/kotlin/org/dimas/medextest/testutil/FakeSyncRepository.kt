package org.dimas.medextest.testutil

import kotlinx.coroutines.delay
import org.dimas.medextest.core.common.NetworkException
import org.dimas.medextest.feature.more.loadlab.domain.repository.SyncRepository
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalAtomicApi::class)
class FakeSyncRepository(
    private val latency: Duration = 5.milliseconds,
    private val failingOnce: Set<Int> = emptySet(),
    private val alwaysFailing: Set<Int> = emptySet(),
) : SyncRepository {
    private val alreadyFailed = mutableSetOf<Int>()
    private val calls = AtomicInt(0)

    val callCount: Int get() = calls.load()

    override suspend fun push(recordId: Int): Int {
        calls.addAndFetch(1)
        delay(latency)
        if (recordId in alwaysFailing) throw NetworkException("POST /sync/$recordId")
        if (recordId in failingOnce && alreadyFailed.add(recordId)) throw NetworkException("POST /sync/$recordId")
        return recordId % REVISIONS + 1
    }

    companion object {
        const val REVISIONS = 97
    }
}
