package org.dimas.medextest.feature.home

import kotlinx.coroutines.test.runTest
import org.dimas.medextest.feature.home.data.MockHomeRepository
import org.dimas.medextest.feature.home.domain.model.WaterIntake
import org.dimas.medextest.feature.home.domain.usecase.AddWaterGlassUseCase
import org.dimas.medextest.testutil.instantBackend
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WaterTest {
    @Test
    fun glass_is_added_until_the_goal_and_never_beyond() = runTest {
        val repository = MockHomeRepository(instantBackend())
        val addGlass = AddWaterGlassUseCase(repository)
        var water = repository.getWaterIntake()

        repeat(10) { water = addGlass(water) }

        assertEquals(water.goalMl, water.consumedMl)
        assertTrue(water.isGoalReached)
    }

    @Test
    fun plus_glass_is_capped_by_the_goal() {
        assertEquals(1500, WaterIntake(consumedMl = 1400, goalMl = 1500, glassMl = 250).plusGlass().consumedMl)
    }
}
