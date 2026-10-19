package org.dimas.medextest.testutil

import org.dimas.medextest.core.mock.FailureMode
import org.dimas.medextest.core.mock.MockBackend
import org.dimas.medextest.core.mock.MockBackendConfig
import kotlin.time.Duration

fun instantBackend(
    failureMode: FailureMode = FailureMode.Never,
    forceEmpty: Boolean = false,
    latency: Duration = Duration.ZERO,
): MockBackend = MockBackend(
    configSource = MutableConfigSource(MockBackendConfig(latency = latency, failureMode = failureMode, forceEmpty = forceEmpty)),
    jitterRatio = 0.0,
)
