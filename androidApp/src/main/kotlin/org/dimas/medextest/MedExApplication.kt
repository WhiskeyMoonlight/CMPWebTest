package org.dimas.medextest

import android.app.Application
import org.dimas.medextest.app.initMedExAndroid

class MedExApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initMedExAndroid(this)
    }
}
