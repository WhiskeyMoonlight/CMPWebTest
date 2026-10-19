package org.dimas.medextest.feature.profile.domain.usecase

import org.dimas.medextest.feature.profile.domain.model.Profile
import org.dimas.medextest.feature.profile.domain.repository.ProfileRepository

class GetProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(): Profile = repository.getProfile()
}
