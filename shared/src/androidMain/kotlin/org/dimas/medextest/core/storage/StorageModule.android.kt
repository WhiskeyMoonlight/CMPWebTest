package org.dimas.medextest.core.storage

import org.koin.core.module.Module
import org.koin.dsl.module

actual val storageModule: Module = module {
    single<KeyValueStorage> { AndroidKeyValueStorage(get()) }
}
