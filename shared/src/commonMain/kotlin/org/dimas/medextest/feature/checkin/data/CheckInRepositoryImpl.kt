package org.dimas.medextest.feature.checkin.data

import kotlinx.datetime.LocalDate
import org.dimas.medextest.core.mock.MockBackend
import org.dimas.medextest.core.storage.KeyValueStorage
import org.dimas.medextest.core.storage.StorageKeys
import org.dimas.medextest.core.storage.getLocalDate
import org.dimas.medextest.core.storage.putLocalDate
import org.dimas.medextest.feature.checkin.domain.model.CheckInEntry
import org.dimas.medextest.feature.checkin.domain.model.Symptom
import org.dimas.medextest.feature.checkin.domain.model.SymptomTone
import org.dimas.medextest.feature.checkin.domain.repository.CheckInRepository

class CheckInRepositoryImpl(
    private val storage: KeyValueStorage,
    private val backend: MockBackend,
) : CheckInRepository {
    override fun lastPromptedOn(): LocalDate? = storage.getLocalDate(StorageKeys.CHECK_IN_LAST_PROMPTED)

    override fun markPrompted(date: LocalDate) = storage.putLocalDate(StorageKeys.CHECK_IN_LAST_PROMPTED, date)

    override fun lastSubmittedOn(): LocalDate? = storage.getLocalDate(StorageKeys.CHECK_IN_LAST_SUBMITTED)

    override suspend fun getSymptoms(): List<Symptom> =
        backend.respond(endpoint = "GET /checkin/symptoms", empty = emptyList()) { SYMPTOMS }

    override suspend fun submit(entry: CheckInEntry, date: LocalDate) {
        backend.call("POST /checkin") { }
        storage.putLocalDate(StorageKeys.CHECK_IN_LAST_SUBMITTED, date)
    }

    override fun reset() {
        storage.remove(StorageKeys.CHECK_IN_LAST_PROMPTED)
        storage.remove(StorageKeys.CHECK_IN_LAST_SUBMITTED)
    }

    private companion object {
        val SYMPTOMS = listOf(
            Symptom("fine", "Всё отлично", SymptomTone.Positive),
            Symptom("insomnia", "Бессонница", SymptomTone.Calm),
            Symptom("headache", "Болит голова", SymptomTone.Negative),
            Symptom("rest", "Отдых", SymptomTone.Leisure),
            Symptom("fatigue", "Усталость", SymptomTone.Negative),
            Symptom("reading", "Чтение", SymptomTone.Leisure),
            Symptom("workout", "Тренировка", SymptomTone.Activity),
            Symptom("cold", "Простуда", SymptomTone.Negative),
            Symptom("stress", "Стресс", SymptomTone.Negative),
            Symptom("walk", "Прогулка", SymptomTone.Activity),
            Symptom("meditation", "Медитация", SymptomTone.Leisure),
            Symptom("nausea", "Тошнота", SymptomTone.Negative),
        )
    }
}
