package org.dimas.medextest.feature.more.loadlab

import kotlinx.coroutines.test.runTest
import org.dimas.medextest.core.background.TaskProgress
import org.dimas.medextest.core.common.NetworkException
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunLabImportUseCase
import org.dimas.medextest.testutil.FakeLabRepository
import org.dimas.medextest.testutil.LocalWorkerBackend
import org.dimas.medextest.testutil.testExecutor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RunLabImportUseCaseTest {
    @Test
    fun every_mode_imports_the_same_records() = runTest {
        val summaries = ExecutionMode.entries.associateWith { mode ->
            val useCase = RunLabImportUseCase(FakeLabRepository(), testExecutor(workers = LocalWorkerBackend()), pages = 5)
            useCase(mode, LoadScale.X1, TaskProgress())
        }

        assertEquals(1, summaries.values.toSet().size, summaries.toString())
        assertTrue(summaries.getValue(ExecutionMode.Default).startsWith("100 записей"))
    }

    @Test
    fun scale_multiplies_page_count_and_progress_completes() = runTest {
        val progress = TaskProgress()
        val useCase = RunLabImportUseCase(FakeLabRepository(pageSize = 10), testExecutor(), pages = 3)

        val summary = useCase(ExecutionMode.Cooperative, LoadScale.X4, progress)

        assertTrue(summary.startsWith("120 записей"), summary)
        assertEquals(1f, progress.fraction)
    }

    @Test
    fun page_fetches_are_limited_by_concurrency() = runTest {
        val repository = FakeLabRepository()
        val useCase = RunLabImportUseCase(repository, testExecutor(), pages = 20, fetchConcurrency = 4)

        useCase(ExecutionMode.Default, LoadScale.X1, TaskProgress())

        assertEquals(4, repository.peakInFlight)
    }

    @Test
    fun transient_network_errors_are_retried() = runTest {
        val repository = FakeLabRepository(failuresPerPage = mapOf(0 to 2, 3 to 1))
        val useCase = RunLabImportUseCase(repository, testExecutor(), pages = 5)

        val summary = useCase(ExecutionMode.Default, LoadScale.X1, TaskProgress())

        assertTrue(summary.startsWith("100 записей"), summary)
    }

    @Test
    fun persistent_network_error_fails_import() = runTest {
        val repository = FakeLabRepository(failuresPerPage = mapOf(2 to 3))
        val useCase = RunLabImportUseCase(repository, testExecutor(), pages = 5)

        assertFailsWith<NetworkException> { useCase(ExecutionMode.Default, LoadScale.X1, TaskProgress()) }
    }
}
