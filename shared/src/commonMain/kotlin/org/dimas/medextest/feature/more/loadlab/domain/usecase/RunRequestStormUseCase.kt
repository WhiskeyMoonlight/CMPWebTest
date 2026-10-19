package org.dimas.medextest.feature.more.loadlab.domain.usecase

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.dimas.medextest.core.background.TaskProgress
import org.dimas.medextest.core.common.NetworkException
import org.dimas.medextest.core.common.retryWithBackoff
import org.dimas.medextest.core.compute.ComputeExecutor
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.repository.SyncRepository
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalAtomicApi::class)
class RunRequestStormUseCase(
    private val repository: SyncRepository,
    private val executor: ComputeExecutor,
    private val requests: Int = DEFAULT_REQUESTS,
    private val concurrency: Int = DEFAULT_CONCURRENCY,
) {
    suspend operator fun invoke(mode: ExecutionMode, scale: LoadScale, progress: TaskProgress): String {
        val total = requests * scale.factor
        progress.expect(total)
        val counters = StormCounters()
        val permits = Semaphore(concurrency)
        withContext(executor.dispatcherFor(mode)) {
            coroutineScope {
                repeat(total) { recordId ->
                    launch { permits.withPermit { push(recordId, counters, progress) } }
                }
            }
        }
        return "$total запросов · одновременно до ${counters.peak.load()} · повторов ${counters.retries.load()}" +
            " · ошибок ${counters.failures.load()} · ревизий ${counters.revisions.load()}"
    }

    private suspend fun push(recordId: Int, counters: StormCounters, progress: TaskProgress) {
        counters.enter()
        try {
            val revision = retryWithBackoff(
                attempts = ATTEMPTS,
                initialDelay = RETRY_DELAY,
                onRetry = { counters.retries.addAndFetch(1) },
            ) { repository.push(recordId) }
            counters.revisions.addAndFetch(revision.toLong())
        } catch (error: NetworkException) {
            counters.failures.addAndFetch(1)
        } finally {
            counters.inFlight.addAndFetch(-1)
            progress.advance(1)
        }
    }

    private class StormCounters {
        val inFlight = AtomicInt(0)
        val peak = AtomicInt(0)
        val retries = AtomicInt(0)
        val failures = AtomicInt(0)
        val revisions = AtomicLong(0)

        fun enter() {
            val current = inFlight.addAndFetch(1)
            while (true) {
                val known = peak.load()
                if (current <= known || peak.compareAndSet(known, current)) return
            }
        }
    }

    companion object {
        const val DEFAULT_REQUESTS = 10_000
        const val DEFAULT_CONCURRENCY = 64
        private const val ATTEMPTS = 3
        private val RETRY_DELAY = 50.milliseconds
    }
}
