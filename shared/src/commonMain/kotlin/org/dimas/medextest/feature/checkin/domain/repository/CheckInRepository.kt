package org.dimas.medextest.feature.checkin.domain.repository

import kotlinx.datetime.LocalDate
import org.dimas.medextest.feature.checkin.domain.model.CheckInEntry
import org.dimas.medextest.feature.checkin.domain.model.Symptom

interface CheckInRepository {
    fun lastPromptedOn(): LocalDate?

    fun markPrompted(date: LocalDate)

    fun lastSubmittedOn(): LocalDate?

    suspend fun getSymptoms(): List<Symptom>

    suspend fun submit(entry: CheckInEntry, date: LocalDate)

    fun reset()
}
