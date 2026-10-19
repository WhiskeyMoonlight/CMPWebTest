package org.dimas.medextest.feature.home.domain.repository

import org.dimas.medextest.feature.home.domain.model.HomeDashboard
import org.dimas.medextest.feature.home.domain.model.WaterIntake

interface HomeRepository {
    suspend fun getDashboard(): HomeDashboard

    suspend fun getWaterIntake(): WaterIntake

    suspend fun addWaterGlass(): WaterIntake
}
