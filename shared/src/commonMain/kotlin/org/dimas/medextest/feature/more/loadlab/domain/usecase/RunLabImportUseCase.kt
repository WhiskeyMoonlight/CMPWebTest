package org.dimas.medextest.feature.more.loadlab.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import org.dimas.medextest.compute.job.ComputeOutcome
import org.dimas.medextest.compute.job.LabParseJob
import org.dimas.medextest.compute.lab.LabStats
import org.dimas.medextest.core.background.TaskProgress
import org.dimas.medextest.core.common.retryWithBackoff
import org.dimas.medextest.core.compute.ComputeExecutor
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.repository.LabRepository
import kotlin.time.Duration.Companion.milliseconds

class RunLabImportUseCase(
    private val repository: LabRepository,
    private val executor: ComputeExecutor,
    private val pages: Int = DEFAULT_PAGES,
    private val fetchConcurrency: Int = DEFAULT_FETCH_CONCURRENCY,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(mode: ExecutionMode, scale: LoadScale, progress: TaskProgress): String {
        val totalPages = pages * scale.factor
        progress.expect(totalPages * LabParseJob.STEPS)
        val stats = (0 until totalPages).asFlow()
            .flatMapMerge(fetchConcurrency) { page -> flow { emit(fetch(page)) } }
            .flatMapMerge(executor.concurrencyOf(mode)) { json ->
                flow { emit(executor.execute(LabParseJob(json), mode, progress) as ComputeOutcome.Lab) }
            }
            .fold(LabStats.EMPTY) { total, outcome -> total.merge(outcome.stats) }
        val outOfRangePercent = if (stats.records == 0) 0 else stats.outOfRange * PERCENT / stats.records
        return "${stats.records} записей · ${stats.analytes.size} показателей · вне нормы $outOfRangePercent%"
    }

    private suspend fun fetch(page: Int): String =
        retryWithBackoff(attempts = ATTEMPTS, initialDelay = RETRY_DELAY) { repository.fetchPage(page) }

    companion object {
        const val DEFAULT_PAGES = 40
        const val DEFAULT_FETCH_CONCURRENCY = 6
        private const val ATTEMPTS = 3
        private const val PERCENT = 100
        private val RETRY_DELAY = 100.milliseconds
    }
}
