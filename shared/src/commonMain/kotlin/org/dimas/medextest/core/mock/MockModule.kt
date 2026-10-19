package org.dimas.medextest.core.mock

import org.koin.dsl.module

val mockModule = module {
    single { MockBackend(get()) }
}
