package org.dimas.medextest.feature.checkin.presentation.mvi

sealed interface CheckInSideEffect {
    data class Close(val submitted: Boolean) : CheckInSideEffect
}
