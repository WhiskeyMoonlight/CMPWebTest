package org.dimas.medextest.core.compute

import org.dimas.medextest.core.platform.PlatformInfo
import org.koin.core.module.Module
import org.koin.dsl.module

actual val computePlatformModule: Module = module {
    single<UiYielder> { MessageChannelYielder() }
    single<WorkerBackend> {
        WebWorkerBackend(scriptUrl = workerScriptPath, poolSize = workerPoolSize(get<PlatformInfo>().cpuCores))
    }
}
