package org.dimas.medextest.feature.home.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.core.common.UiMessage
import org.dimas.medextest.core.common.runCatchingCancellable
import org.dimas.medextest.core.common.toLoadState
import org.dimas.medextest.feature.checkin.domain.model.Mood
import org.dimas.medextest.feature.checkin.domain.usecase.MarkCheckInPromptedUseCase
import org.dimas.medextest.feature.checkin.domain.usecase.ShouldShowDailyCheckInUseCase
import org.dimas.medextest.feature.home.domain.model.QuickAction
import org.dimas.medextest.feature.home.domain.usecase.AddWaterGlassUseCase
import org.dimas.medextest.feature.home.domain.usecase.GetGreetingUseCase
import org.dimas.medextest.feature.home.domain.usecase.GetHomeDashboardUseCase
import org.dimas.medextest.feature.home.domain.usecase.GetWaterIntakeUseCase
import org.dimas.medextest.feature.home.presentation.model.CheckInLaunch
import org.dimas.medextest.feature.home.presentation.mvi.HomeActions
import org.dimas.medextest.feature.home.presentation.mvi.HomeSideEffect
import org.dimas.medextest.feature.home.presentation.mvi.HomeState
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.orbitContainer
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModel(
    private val getGreeting: GetGreetingUseCase,
    private val getDashboard: GetHomeDashboardUseCase,
    private val getWaterIntake: GetWaterIntakeUseCase,
    private val addWaterGlass: AddWaterGlassUseCase,
    private val shouldShowDailyCheckIn: ShouldShowDailyCheckInUseCase,
    private val markCheckInPrompted: MarkCheckInPromptedUseCase,
) : ViewModel(), OrbitContainerHost<HomeState, HomeState, HomeSideEffect>, HomeActions {

    override val container: OrbitContainer<HomeState, HomeState, HomeSideEffect> = orbitContainer(
        initialState = HomeState(),
    ) {
        coroutineScope {
            launch { loadGreeting() }
            launch { loadDashboard() }
            launch { loadWater() }
            launch { repeatOnSubscription { promptCheckInIfNeeded() } }
        }
    }

    override fun onRetry() {
        intent {
            coroutineScope {
                if (state.dashboard is LoadState.Error) launch { loadDashboard() }
                if (state.water is LoadState.Error) launch { loadWater() }
            }
        }
    }

    override fun onQuickActionClick(action: QuickAction) {
        intent { postSideEffect(HomeSideEffect.ShowMessage(UiMessage.ComingSoon)) }
    }

    override fun onFillHealthProfileClick() {
        intent { postSideEffect(HomeSideEffect.ShowMessage(UiMessage.ComingSoon)) }
    }

    override fun onCalendarClick() {
        intent { postSideEffect(HomeSideEffect.ShowMessage(UiMessage.ComingSoon)) }
    }

    override fun onMoodClick(mood: Mood) {
        intent { reduce { state.withCheckIn(preselectedMood = mood) } }
    }

    override fun onAddWaterClick() {
        intent {
            val current = (state.water as? LoadState.Content)?.data ?: return@intent
            if (state.isAddingWater || current.isGoalReached) return@intent
            reduce { state.copy(isAddingWater = true, water = LoadState.Content(current.plusGlass())) }
            val result = runCatchingCancellable { addWaterGlass(current) }
            reduce { state.copy(isAddingWater = false, water = LoadState.Content(result.getOrDefault(current))) }
            if (result.isFailure) postSideEffect(HomeSideEffect.ShowMessage(UiMessage.NetworkError))
        }
    }

    override fun onCheckInClosed(submitted: Boolean) {
        intent {
            reduce { state.copy(checkIn = null) }
            if (submitted) postSideEffect(HomeSideEffect.ShowMessage(UiMessage.CheckInSaved))
        }
    }

    private suspend fun Syntax<HomeState, HomeSideEffect>.loadGreeting() {
        runCatchingCancellable { getGreeting() }.onSuccess { greeting ->
            reduce { state.copy(greeting = greeting) }
        }
    }

    private suspend fun Syntax<HomeState, HomeSideEffect>.loadDashboard() {
        reduce { state.copy(dashboard = LoadState.Loading) }
        val result = runCatchingCancellable { getDashboard() }
        reduce { state.copy(dashboard = result.toLoadState { it.quickActions.isEmpty() }) }
    }

    private suspend fun Syntax<HomeState, HomeSideEffect>.loadWater() {
        reduce { state.copy(water = LoadState.Loading) }
        val result = runCatchingCancellable { getWaterIntake() }
        reduce { state.copy(water = result.toLoadState()) }
    }

    private suspend fun Syntax<HomeState, HomeSideEffect>.promptCheckInIfNeeded() {
        if (state.checkIn != null || !shouldShowDailyCheckIn()) return
        delay(CHECK_IN_PROMPT_DELAY)
        markCheckInPrompted()
        reduce { state.withCheckIn(preselectedMood = null) }
    }

    private companion object {
        val CHECK_IN_PROMPT_DELAY = 700.milliseconds
    }
}

private fun HomeState.withCheckIn(preselectedMood: Mood?): HomeState {
    val launches = checkInLaunches + 1
    return copy(checkIn = CheckInLaunch(requestId = launches, preselectedMood = preselectedMood), checkInLaunches = launches)
}
