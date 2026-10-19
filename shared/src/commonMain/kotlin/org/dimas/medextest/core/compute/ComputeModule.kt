package org.dimas.medextest.core.compute

import org.dimas.medextest.core.coroutines.DispatcherQualifier
import org.dimas.medextest.core.platform.PlatformInfo
import org.koin.core.qualifier.named
import org.koin.dsl.module

val computeModule = module {
    single {
        ComputeExecutor(
            mainDispatcher = get(named(DispatcherQualifier.Main)),
            defaultDispatcher = get(named(DispatcherQualifier.Default)),
            uiYielder = get(),
            workers = get(),
            cpuCores = get<PlatformInfo>().cpuCores,
        )
    }
}
