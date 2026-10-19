package org.dimas.medextest.feature.more.capabilities.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.dimas.medextest.core.common.runCatchingCancellable
import org.dimas.medextest.core.location.LocationException
import org.dimas.medextest.core.location.LocationPermission
import org.dimas.medextest.core.location.LocationService
import org.dimas.medextest.core.media.PickedFile
import org.dimas.medextest.core.platform.PlatformInfo
import org.dimas.medextest.feature.more.capabilities.presentation.model.AttachmentUi
import org.dimas.medextest.feature.more.capabilities.presentation.model.LocationUi
import org.dimas.medextest.feature.more.capabilities.presentation.mvi.CapabilitiesActions
import org.dimas.medextest.feature.more.capabilities.presentation.mvi.CapabilitiesSideEffect
import org.dimas.medextest.feature.more.capabilities.presentation.mvi.CapabilitiesState
import org.dimas.medextest.feature.more.capabilities.presentation.util.isImage
import org.dimas.medextest.feature.more.capabilities.presentation.util.toLocationMessage
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer
import kotlin.time.TimeSource

class CapabilitiesViewModel(
    private val locationService: LocationService,
    private val defaultDispatcher: CoroutineDispatcher,
    platformInfo: PlatformInfo,
) : ViewModel(),
    OrbitContainerHost<CapabilitiesState, CapabilitiesState, CapabilitiesSideEffect>,
    CapabilitiesActions {

    override val container: OrbitContainer<CapabilitiesState, CapabilitiesState, CapabilitiesSideEffect> =
        orbitContainer(initialState = CapabilitiesState(platform = platformInfo))

    override fun onLocateClick() {
        intent {
            if (state.location == LocationUi.Requesting) return@intent
            reduce { state.copy(location = LocationUi.Requesting) }
            val started = TimeSource.Monotonic.markNow()
            val result = runCatchingCancellable {
                if (locationService.requestPermission() == LocationPermission.Denied) throw LocationException.PermissionDenied()
                locationService.currentLocation()
            }
            val location = result.fold(
                onSuccess = { LocationUi.Located(it, started.elapsedNow().inWholeMilliseconds) },
                onFailure = { LocationUi.Failed(it.toLocationMessage()) },
            )
            reduce { state.copy(location = location) }
        }
    }

    override fun onFilePicked(file: PickedFile?) {
        if (file == null) return
        intent {
            reduce { state.copy(attachment = AttachmentUi.Reading(file.name)) }
            val result = runCatchingCancellable { readAttachment(file) }
            reduce {
                state.copy(
                    attachment = result.getOrElse { AttachmentUi.Failed(it.message ?: "Не удалось прочитать файл") },
                )
            }
        }
    }

    override fun onPickerError(error: Throwable) {
        intent {
            reduce {
                state.copy(attachment = AttachmentUi.Failed(error.message ?: "Не удалось открыть выбор файла"))
            }
        }
    }

    override fun onPromoCopied() {
        intent { postSideEffect(CapabilitiesSideEffect.ShowMessage("Промокод скопирован")) }
    }

    private suspend fun readAttachment(file: PickedFile): AttachmentUi.Ready {
        val readStarted = TimeSource.Monotonic.markNow()
        val bytes = file.readBytes()
        val readMs = readStarted.elapsedNow().inWholeMilliseconds
        val decoded = if (file.isImage()) {
            withContext(defaultDispatcher) {
                val decodeStarted = TimeSource.Monotonic.markNow()
                bytes.decodeToImageBitmap() to decodeStarted.elapsedNow().inWholeMilliseconds
            }
        } else {
            null
        }
        return AttachmentUi.Ready(
            name = file.name,
            mimeType = file.mimeType,
            sizeBytes = file.sizeBytes,
            preview = decoded?.first,
            readMs = readMs,
            decodeMs = decoded?.second,
        )
    }
}
