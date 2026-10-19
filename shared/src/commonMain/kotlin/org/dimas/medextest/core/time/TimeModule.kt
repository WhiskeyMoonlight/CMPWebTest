package org.dimas.medextest.core.time

import org.koin.dsl.module

val timeModule = module {
    single { TimeProvider() }
}
