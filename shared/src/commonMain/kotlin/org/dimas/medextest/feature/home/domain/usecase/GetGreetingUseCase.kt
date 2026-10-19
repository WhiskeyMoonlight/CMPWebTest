package org.dimas.medextest.feature.home.domain.usecase

import org.dimas.medextest.core.time.TimeProvider
import org.dimas.medextest.feature.home.domain.model.DayPart
import org.dimas.medextest.feature.home.domain.model.Greeting
import org.dimas.medextest.feature.profile.domain.repository.ProfileRepository

class GetGreetingUseCase(
    private val profiles: ProfileRepository,
    private val time: TimeProvider,
) {
    suspend operator fun invoke(): Greeting {
        val profile = profiles.getProfile()
        return Greeting(
            dayPart = DayPart.of(time.now().hour),
            firstName = profile.firstName,
            initials = profile.initials,
            avatarSeed = profile.avatarSeed,
        )
    }
}
