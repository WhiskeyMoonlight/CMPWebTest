package org.dimas.medextest.core.mock

import kotlinx.coroutines.delay
import org.dimas.medextest.core.common.NetworkException
import kotlin.random.Random
import kotlin.time.Duration

class MockBackend(
    private val configSource: MockBackendConfigSource,
    private val random: Random = Random.Default,
    private val jitterRatio: Double = DEFAULT_JITTER_RATIO,
) {
    suspend fun <T> respond(endpoint: String, empty: T, produce: () -> T): T {
        val config = configSource.config.value
        delay(withJitter(config.latency))
        if (random.nextDouble() < config.failureMode.probability) throw NetworkException(endpoint)
        return if (config.forceEmpty) empty else produce()
    }

    suspend fun <T> call(endpoint: String, latency: Duration? = null, produce: () -> T): T {
        val config = configSource.config.value
        delay(withJitter(latency ?: config.latency))
        if (random.nextDouble() < config.failureMode.probability) throw NetworkException(endpoint)
        return produce()
    }

    private fun withJitter(latency: Duration): Duration {
        if (latency == Duration.ZERO || jitterRatio == 0.0) return latency
        val factor = 1.0 + random.nextDouble(-jitterRatio, jitterRatio)
        return latency * factor
    }

    private companion object {
        const val DEFAULT_JITTER_RATIO = 0.25
    }
}
