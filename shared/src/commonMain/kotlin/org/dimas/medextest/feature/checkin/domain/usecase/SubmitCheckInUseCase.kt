package org.dimas.medextest.feature.checkin.domain.usecase

import org.dimas.medextest.core.time.TimeProvider
import org.dimas.medextest.feature.checkin.domain.model.CheckInEntry
import org.dimas.medextest.feature.checkin.domain.repository.CheckInRepository

class SubmitCheckInUseCase(
    private val repository: CheckInRepository,
    private val time: TimeProvider,
) {
    suspend operator fun invoke(entry: CheckInEntry) {
        require(!entry.isEmpty) { "Empty check-in must not be submitted" }
        require(entry.wellbeing == null || entry.wellbeing in 1..WELLBEING_MAX) { "Wellbeing must be in 1..$WELLBEING_MAX" }
        repository.submit(entry.copy(note = entry.note.trim()), time.today())
    }
}
