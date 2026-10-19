package org.dimas.medextest.feature.more.capabilities.presentation.mvi

import androidx.compose.runtime.Stable
import org.dimas.medextest.core.media.PickedFile

@Stable
interface CapabilitiesActions {
    fun onLocateClick()

    fun onFilePicked(file: PickedFile?)

    fun onPickerError(error: Throwable)

    fun onPromoCopied()
}
