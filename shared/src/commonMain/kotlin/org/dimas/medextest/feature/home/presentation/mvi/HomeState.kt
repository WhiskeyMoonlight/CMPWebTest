package org.dimas.medextest.feature.home.presentation.mvi

import androidx.compose.runtime.Immutable
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.feature.home.domain.model.Greeting
import org.dimas.medextest.feature.home.domain.model.HomeDashboard
import org.dimas.medextest.feature.home.domain.model.WaterIntake
import org.dimas.medextest.feature.home.presentation.model.CheckInLaunch

@Immutable
data class HomeState(
    val greeting: Greeting? = null,
    val dashboard: LoadState<HomeDashboard> = LoadState.Loading,
    val water: LoadState<WaterIntake> = LoadState.Loading,
    val isAddingWater: Boolean = false,
    val checkIn: CheckInLaunch? = null,
    val checkInLaunches: Int = 0,
)
