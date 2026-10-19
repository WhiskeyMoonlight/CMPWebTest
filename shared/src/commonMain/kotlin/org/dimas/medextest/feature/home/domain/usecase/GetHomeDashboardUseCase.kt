package org.dimas.medextest.feature.home.domain.usecase

import org.dimas.medextest.feature.home.domain.model.HomeDashboard
import org.dimas.medextest.feature.home.domain.repository.HomeRepository

class GetHomeDashboardUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(): HomeDashboard = repository.getDashboard()
}
