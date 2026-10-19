package org.dimas.medextest.feature.more.presentation

import androidx.lifecycle.ViewModel
import org.dimas.medextest.core.common.UiMessage
import org.dimas.medextest.core.common.runCatchingCancellable
import org.dimas.medextest.feature.more.presentation.model.MoreMenuItem
import org.dimas.medextest.feature.more.presentation.mvi.MoreActions
import org.dimas.medextest.feature.more.presentation.mvi.MoreSideEffect
import org.dimas.medextest.feature.more.presentation.mvi.MoreState
import org.dimas.medextest.feature.profile.domain.usecase.GetProfileUseCase
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class MoreViewModel(
    private val getProfile: GetProfileUseCase,
) : ViewModel(), OrbitContainerHost<MoreState, MoreState, MoreSideEffect>, MoreActions {

    override val container: OrbitContainer<MoreState, MoreState, MoreSideEffect> =
        orbitContainer(initialState = MoreState()) {
            val profile = runCatchingCancellable { getProfile() }.getOrNull()
            reduce { state.copy(profile = profile) }
        }

    override fun onMenuItemClick(item: MoreMenuItem) {
        intent { postSideEffect(MoreSideEffect.ShowMessage(UiMessage.ComingSoon)) }
    }
}
