package org.dimas.medextest.core.platform

data class PlatformInfo(
    val name: String,
    val kind: PlatformKind,
    val cpuCores: Int = 1,
)
