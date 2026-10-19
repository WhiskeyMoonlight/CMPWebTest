package org.dimas.medextest.feature.home.domain.usecase

import org.dimas.medextest.feature.home.domain.model.WaterIntake
import org.dimas.medextest.feature.home.domain.repository.HomeRepository

class AddWaterGlassUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(current: WaterIntake): WaterIntake =
        if (current.isGoalReached) current else repository.addWaterGlass()
}
