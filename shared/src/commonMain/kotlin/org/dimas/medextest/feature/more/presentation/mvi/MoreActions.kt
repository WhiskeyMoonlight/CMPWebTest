package org.dimas.medextest.feature.more.presentation.mvi

import androidx.compose.runtime.Stable
import org.dimas.medextest.feature.more.presentation.model.MoreMenuItem

@Stable
interface MoreActions {
    fun onMenuItemClick(item: MoreMenuItem)
}
