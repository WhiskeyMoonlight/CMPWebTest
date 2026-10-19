package org.dimas.medextest.feature.checkin.domain.usecase

import org.dimas.medextest.core.time.TimeProvider
import org.dimas.medextest.feature.checkin.domain.repository.CheckInRepository

class ShouldShowDailyCheckInUseCase(
    private val repository: CheckInRepository,
    private val time: TimeProvider,
) {
    operator fun invoke(): Boolean {
        val today = time.today()
        return repository.lastSubmittedOn() != today && repository.lastPromptedOn() != today
    }
}
