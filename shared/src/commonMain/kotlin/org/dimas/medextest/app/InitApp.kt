package org.dimas.medextest.app

import kotlinx.coroutines.CoroutineExceptionHandler
import org.dimas.medextest.app.di.appModules
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.mp.KoinPlatform
import org.orbitmvi.orbit.Orbit

fun initApp(platformDeclaration: KoinAppDeclaration = {}) {
    if (KoinPlatform.getKoinOrNull() != null) return
    Orbit.configureDefaults {
        exceptionHandler = CoroutineExceptionHandler { _, error ->
            println("Orbit: unhandled ${error::class.simpleName}: ${error.message}")
        }
    }
    startKoin {
        platformDeclaration()
        modules(appModules)
    }
}
