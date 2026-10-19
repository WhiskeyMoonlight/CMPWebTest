package org.dimas.medextest.core.background

import org.dimas.medextest.core.coroutines.ScopeQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

val backgroundModule = module {
    single { BackgroundTasks(scope = get(named(ScopeQualifier.Application)), perfMonitor = get()) }
}
