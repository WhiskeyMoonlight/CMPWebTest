package org.dimas.medextest.core.perf

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.dimas.medextest.core.storage.KeyValueStorage
import org.dimas.medextest.core.storage.StorageKeys
import org.dimas.medextest.core.storage.getBoolean
import org.dimas.medextest.core.storage.putBoolean

class PerfHudPreference(private val storage: KeyValueStorage) {
    private val state = MutableStateFlow(storage.getBoolean(StorageKeys.DEV_PERF_HUD))

    val isEnabled: StateFlow<Boolean> = state.asStateFlow()

    fun setEnabled(enabled: Boolean) {
        state.value = enabled
        storage.putBoolean(StorageKeys.DEV_PERF_HUD, enabled)
    }
}
