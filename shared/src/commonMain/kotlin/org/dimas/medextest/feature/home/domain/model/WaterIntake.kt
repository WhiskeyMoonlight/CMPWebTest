package org.dimas.medextest.feature.home.domain.model

data class WaterIntake(
    val consumedMl: Int,
    val goalMl: Int,
    val glassMl: Int,
) {
    val glassCount: Int get() = goalMl / glassMl
    val filledGlasses: Int get() = consumedMl / glassMl
    val isGoalReached: Boolean get() = consumedMl >= goalMl

    fun plusGlass(): WaterIntake = copy(consumedMl = minOf(consumedMl + glassMl, goalMl))
}
