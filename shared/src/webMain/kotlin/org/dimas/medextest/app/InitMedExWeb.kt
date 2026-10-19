package org.dimas.medextest.app

import org.dimas.medextest.feature.more.loadlab.debug.installPerfBridgeIfRequested

fun initMedExWeb() {
    initApp()
    installPerfBridgeIfRequested()
}
