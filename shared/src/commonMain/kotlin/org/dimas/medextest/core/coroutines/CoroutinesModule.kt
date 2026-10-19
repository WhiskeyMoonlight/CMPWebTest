package org.dimas.medextest.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutinesModule = module {
    single<CoroutineDispatcher>(named(DispatcherQualifier.Main)) { Dispatchers.Main }
    single<CoroutineDispatcher>(named(DispatcherQualifier.Default)) { Dispatchers.Default }
    single<CoroutineScope>(named(ScopeQualifier.Application)) {
        val handler = CoroutineExceptionHandler { _, error ->
            println("Application scope: unhandled ${error::class.simpleName}: ${error.message}")
        }
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(DispatcherQualifier.Default)) + handler)
    }
}
