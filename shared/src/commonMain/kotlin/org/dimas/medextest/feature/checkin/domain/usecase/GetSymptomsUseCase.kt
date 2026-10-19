package org.dimas.medextest.feature.checkin.domain.usecase

import org.dimas.medextest.feature.checkin.domain.model.Symptom
import org.dimas.medextest.feature.checkin.domain.repository.CheckInRepository

class GetSymptomsUseCase(private val repository: CheckInRepository) {
    suspend operator fun invoke(): List<Symptom> = repository.getSymptoms()
}
