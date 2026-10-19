package org.dimas.medextest.core.platform

import android.os.Build
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { AndroidPermissionRequester() }
    single {
        PlatformInfo(
            name = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
            kind = PlatformKind.Android,
            cpuCores = Runtime.getRuntime().availableProcessors(),
        )
    }
}
