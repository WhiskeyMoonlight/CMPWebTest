package org.dimas.medextest.feature.home.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.dimas.medextest.core.mock.MockBackend
import org.dimas.medextest.feature.home.domain.model.HomeDashboard
import org.dimas.medextest.feature.home.domain.model.QuickAction
import org.dimas.medextest.feature.home.domain.model.WaterIntake
import org.dimas.medextest.feature.home.domain.repository.HomeRepository

class MockHomeRepository(private val backend: MockBackend) : HomeRepository {
    private val waterLock = Mutex()
    private var water = WaterIntake(consumedMl = 250, goalMl = 1500, glassMl = 250)

    override suspend fun getDashboard(): HomeDashboard = backend.respond(
        endpoint = "GET /home/dashboard",
        empty = HomeDashboard(quickActions = emptyList(), clinicPhone = CLINIC_PHONE, showHealthProfilePromo = false),
    ) {
        HomeDashboard(quickActions = QuickAction.entries, clinicPhone = CLINIC_PHONE, showHealthProfilePromo = true)
    }

    override suspend fun getWaterIntake(): WaterIntake {
        val snapshot = waterLock.withLock { water }
        return backend.respond(endpoint = "GET /home/water", empty = snapshot.copy(consumedMl = 0)) { snapshot }
    }

    override suspend fun addWaterGlass(): WaterIntake {
        backend.call("POST /home/water") { }
        return waterLock.withLock {
            water = water.plusGlass()
            water
        }
    }
}
