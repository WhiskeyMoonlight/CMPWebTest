package org.dimas.medextest.testutil

import kotlinx.datetime.LocalDate
import org.dimas.medextest.feature.checkin.domain.model.CheckInEntry
import org.dimas.medextest.feature.checkin.domain.model.Symptom
import org.dimas.medextest.feature.checkin.domain.model.SymptomTone
import org.dimas.medextest.feature.checkin.domain.repository.CheckInRepository

class FakeCheckInRepository : CheckInRepository {
    var lastPrompted: LocalDate? = null
    var lastSubmitted: LocalDate? = null
    val submitted = mutableListOf<CheckInEntry>()

    override fun lastPromptedOn(): LocalDate? = lastPrompted

    override fun markPrompted(date: LocalDate) {
        lastPrompted = date
    }

    override fun lastSubmittedOn(): LocalDate? = lastSubmitted

    override suspend fun getSymptoms(): List<Symptom> = listOf(Symptom("fine", "Всё отлично", SymptomTone.Positive))

    override suspend fun submit(entry: CheckInEntry, date: LocalDate) {
        submitted += entry
        lastSubmitted = date
    }

    override fun reset() {
        lastPrompted = null
        lastSubmitted = null
    }
}
