package org.dimas.medextest.core.location

import org.koin.core.module.Module
import org.koin.dsl.module

actual val locationModule: Module = module {
    single<LocationService> { AndroidLocationService(get(), get(), get()) }
}
