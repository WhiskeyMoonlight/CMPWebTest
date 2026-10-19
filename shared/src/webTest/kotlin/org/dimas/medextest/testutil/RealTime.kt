package org.dimas.medextest.testutil

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

private val POLL_INTERVAL = 16.milliseconds

suspend fun realDelay(duration: Duration) = withContext(Dispatchers.Default) { delay(duration) }

@OptIn(ExperimentalTestApi::class)
suspend fun ComposeUiTest.awaitCondition(
    description: String,
    timeout: Duration = 15.seconds,
    condition: () -> Boolean,
) {
    val started = TimeSource.Monotonic.markNow()
    while (true) {
        awaitIdle()
        if (condition()) return
        check(started.elapsedNow() < timeout) { "Не дождались: $description за $timeout" }
        realDelay(POLL_INTERVAL)
    }
}
