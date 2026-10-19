package org.dimas.medextest.core.platform

import org.koin.core.module.Module
import org.koin.dsl.module
import web.navigator.navigator

actual val platformModule: Module = module {
    single {
        PlatformInfo(
            name = "${browserName()} · $webRuntimeName",
            kind = PlatformKind.Web,
            cpuCores = navigator.hardwareConcurrency.toInt().coerceAtLeast(1),
        )
    }
}
