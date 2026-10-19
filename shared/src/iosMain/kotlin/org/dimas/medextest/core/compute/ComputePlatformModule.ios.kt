package org.dimas.medextest.core.compute

import org.koin.core.module.Module
import org.koin.dsl.module

actual val computePlatformModule: Module = module {
    single<UiYielder> { CoroutineYielder() }
    single<WorkerBackend> { NoWorkerBackend }
}
