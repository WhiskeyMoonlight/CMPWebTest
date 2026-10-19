package org.dimas.medextest.feature.more.devsettings.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import org.dimas.medextest.core.mock.FailureMode
import org.dimas.medextest.core.mock.MockBackendConfig
import org.dimas.medextest.core.mock.MockBackendConfigSource
import org.dimas.medextest.core.storage.KeyValueStorage
import org.dimas.medextest.core.storage.StorageKeys
import org.dimas.medextest.core.storage.getBoolean
import org.dimas.medextest.core.storage.putBoolean
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class DevSettingsRepository(private val storage: KeyValueStorage) : MockBackendConfigSource {
    private val state = MutableStateFlow(load())

    override val config: StateFlow<MockBackendConfig> = state.asStateFlow()

    fun setLatency(latency: Duration) = update { copy(latency = latency) }

    fun setFailureMode(mode: FailureMode) = update { copy(failureMode = mode) }

    fun setForceEmpty(enabled: Boolean) = update { copy(forceEmpty = enabled) }

    private fun update(transform: MockBackendConfig.() -> MockBackendConfig) {
        val updated = state.updateAndGet(transform)
        storage.putString(StorageKeys.DEV_LATENCY_MS, updated.latency.inWholeMilliseconds.toString())
        storage.putString(StorageKeys.DEV_FAILURE_MODE, updated.failureMode.name)
        storage.putBoolean(StorageKeys.DEV_FORCE_EMPTY, updated.forceEmpty)
    }

    private fun load(): MockBackendConfig = MockBackendConfig(
        latency = storage.getString(StorageKeys.DEV_LATENCY_MS)?.toLongOrNull()?.milliseconds ?: MockBackendConfig.DEFAULT_LATENCY,
        failureMode = storage.getString(StorageKeys.DEV_FAILURE_MODE)
            ?.let { name -> FailureMode.entries.firstOrNull { it.name == name } }
            ?: FailureMode.Never,
        forceEmpty = storage.getBoolean(StorageKeys.DEV_FORCE_EMPTY),
    )
}
