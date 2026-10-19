package org.dimas.medextest.feature.profile.data

import org.dimas.medextest.core.mock.MockBackend
import org.dimas.medextest.feature.profile.domain.model.Profile
import org.dimas.medextest.feature.profile.domain.repository.ProfileRepository

class MockProfileRepository(private val backend: MockBackend) : ProfileRepository {
    override suspend fun getProfile(): Profile = backend.call("GET /profile") {
        Profile(firstName = "Анна", lastName = "Морозова", avatarSeed = 0)
    }
}
