package org.dimas.medextest.core.compute

internal expect val workerScriptPath: String

internal fun workerPoolSize(cpuCores: Int): Int = (cpuCores - 1).coerceIn(MIN_WORKERS, MAX_WORKERS)

private const val MIN_WORKERS = 1
private const val MAX_WORKERS = 8
