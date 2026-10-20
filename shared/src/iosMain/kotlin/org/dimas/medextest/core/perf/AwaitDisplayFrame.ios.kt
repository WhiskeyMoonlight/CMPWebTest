package org.dimas.medextest.core.perf

import androidx.compose.runtime.withFrameNanos

internal actual suspend fun awaitDisplayFrame(): Long = withFrameNanos { it }
