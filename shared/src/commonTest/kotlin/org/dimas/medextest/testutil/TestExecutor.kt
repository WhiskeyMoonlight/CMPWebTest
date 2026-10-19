package org.dimas.medextest.testutil

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.dimas.medextest.core.compute.ComputeExecutor
import org.dimas.medextest.core.compute.CoroutineYielder
import org.dimas.medextest.core.compute.NoWorkerBackend
import org.dimas.medextest.core.compute.UiYielder
import org.dimas.medextest.core.compute.WorkerBackend
import kotlin.time.Duration

fun TestScope.testExecutor(
    workers: WorkerBackend = NoWorkerBackend,
    yielder: UiYielder = CoroutineYielder(),
    cpuCores: Int = 4,
    sliceBudget: Duration = ComputeExecutor.DEFAULT_SLICE_BUDGET,
    mainDispatcher: CoroutineDispatcher = StandardTestDispatcher(testScheduler, "main"),
    defaultDispatcher: CoroutineDispatcher = StandardTestDispatcher(testScheduler, "default"),
): ComputeExecutor = ComputeExecutor(
    mainDispatcher = mainDispatcher,
    defaultDispatcher = defaultDispatcher,
    uiYielder = yielder,
    workers = workers,
    cpuCores = cpuCores,
    sliceBudget = sliceBudget,
    timeSource = testScheduler.timeSource,
)
