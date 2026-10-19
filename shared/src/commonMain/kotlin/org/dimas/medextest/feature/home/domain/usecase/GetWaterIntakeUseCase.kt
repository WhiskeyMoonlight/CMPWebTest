package org.dimas.medextest.feature.home.domain.usecase

import org.dimas.medextest.feature.home.domain.model.WaterIntake
import org.dimas.medextest.feature.home.domain.repository.HomeRepository

class GetWaterIntakeUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(): WaterIntake = repository.getWaterIntake()
}
