package org.dimas.medextest.app

import android.content.Context
import org.koin.dsl.module

fun initMedExAndroid(context: Context) {
    val applicationContext = context.applicationContext
    initApp { modules(module { single<Context> { applicationContext } }) }
}
