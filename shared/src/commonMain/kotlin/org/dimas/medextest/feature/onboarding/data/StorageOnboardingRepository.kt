package org.dimas.medextest.feature.onboarding.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.dimas.medextest.core.storage.KeyValueStorage
import org.dimas.medextest.core.storage.StorageKeys
import org.dimas.medextest.core.storage.getBoolean
import org.dimas.medextest.core.storage.putBoolean
import org.dimas.medextest.feature.onboarding.domain.repository.OnboardingRepository

class StorageOnboardingRepository(private val storage: KeyValueStorage) : OnboardingRepository {
    private val completed = MutableStateFlow(storage.getBoolean(StorageKeys.ONBOARDING_COMPLETED))

    override val isCompleted: StateFlow<Boolean> = completed.asStateFlow()

    override fun complete() {
        storage.putBoolean(StorageKeys.ONBOARDING_COMPLETED, true)
        completed.value = true
    }

    override fun reset() {
        storage.remove(StorageKeys.ONBOARDING_COMPLETED)
        completed.value = false
    }
}
