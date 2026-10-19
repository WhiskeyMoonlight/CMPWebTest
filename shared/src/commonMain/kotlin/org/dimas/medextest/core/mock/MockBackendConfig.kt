package org.dimas.medextest.core.mock

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class MockBackendConfig(
    val latency: Duration = DEFAULT_LATENCY,
    val failureMode: FailureMode = FailureMode.Never,
    val forceEmpty: Boolean = false,
) {
    companion object {
        val DEFAULT_LATENCY: Duration = 800.milliseconds
        val LATENCY_OPTIONS: List<Duration> = listOf(0, 300, 800, 3000).map { it.milliseconds }
    }
}
