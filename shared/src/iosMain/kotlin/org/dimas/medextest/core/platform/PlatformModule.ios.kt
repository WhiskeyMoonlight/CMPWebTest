package org.dimas.medextest.core.platform

import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIDevice

actual val platformModule: Module = module {
    single {
        val device = UIDevice.currentDevice
        PlatformInfo(
            name = "${device.systemName()} ${device.systemVersion}",
            kind = PlatformKind.Ios,
            cpuCores = NSProcessInfo.processInfo.activeProcessorCount.toInt(),
        )
    }
}
