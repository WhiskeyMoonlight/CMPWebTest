package org.dimas.medextest.core.perf

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val perfModule = module {
    single { PerfMonitor() }
    singleOf(::PerfHudPreference)
}
