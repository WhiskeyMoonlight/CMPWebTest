package org.dimas.medextest.feature.profile.domain.repository

import org.dimas.medextest.feature.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(): Profile
}
