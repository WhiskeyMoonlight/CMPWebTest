package org.dimas.medextest.feature.checkin.domain.usecase

import org.dimas.medextest.core.time.TimeProvider
import org.dimas.medextest.feature.checkin.domain.repository.CheckInRepository

class MarkCheckInPromptedUseCase(
    private val repository: CheckInRepository,
    private val time: TimeProvider,
) {
    operator fun invoke() = repository.markPrompted(time.today())
}
