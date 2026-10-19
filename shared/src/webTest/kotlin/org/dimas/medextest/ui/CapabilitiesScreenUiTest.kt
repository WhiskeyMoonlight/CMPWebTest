package org.dimas.medextest.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.v2.runComposeUiTest
import org.dimas.medextest.core.designsystem.theme.MedExTheme
import org.dimas.medextest.core.location.GeoPoint
import org.dimas.medextest.core.platform.PlatformInfo
import org.dimas.medextest.core.platform.PlatformKind
import org.dimas.medextest.feature.more.capabilities.presentation.model.AttachmentUi
import org.dimas.medextest.feature.more.capabilities.presentation.model.LocationUi
import org.dimas.medextest.feature.more.capabilities.presentation.mvi.CapabilitiesState
import org.dimas.medextest.feature.more.capabilities.presentation.ui.CapabilitiesScreen
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class CapabilitiesScreenUiTest {
    private val platform = PlatformInfo(name = "Chrome · Kotlin/JS", kind = PlatformKind.Web)

    @Composable
    private fun Screen(state: CapabilitiesState, calls: MutableList<String> = mutableListOf()) {
        MedExTheme {
            CapabilitiesScreen(
                state = state,
                snackbarHostState = SnackbarHostState(),
                onBack = { calls += "back" },
                onPickImage = { calls += "image" },
                onPickFile = { calls += "file" },
                onTakePhoto = { calls += "camera" },
                onLocate = { calls += "locate" },
                onOpenMap = { calls += "map" },
                onCall = { calls += "call" },
                onCopyPromo = { calls += "promo" },
            )
        }
    }

    @Test
    fun native_actions_are_started_from_clicks() = runComposeUiTest {
        val calls = mutableListOf<String>()
        setContent { Screen(CapabilitiesState(platform = platform), calls) }

        listOf("Фото из галереи", "Любой файл", "Снять фото", "Определить геопозицию", "Открыть на карте", "Позвонить в клинику")
            .forEach { onNodeWithText(it).performScrollTo().performClick() }
        onNodeWithText("Скопировать промокод", substring = true).performScrollTo().performClick()

        assertEquals(listOf("image", "file", "camera", "locate", "map", "call", "promo"), calls)
    }

    @Test
    fun attachment_states_are_rendered() = runComposeUiTest {
        var attachment: AttachmentUi = AttachmentUi.Reading("scan.pdf")
        val state = androidx.compose.runtime.mutableStateOf(CapabilitiesState(platform = platform, attachment = attachment))
        setContent { Screen(state.value) }

        onNodeWithText("Читаем «scan.pdf»…").performScrollTo().assertExists()

        attachment = AttachmentUi.Ready("scan.pdf", "application/pdf", 2_048, preview = null, readMs = 12, decodeMs = null)
        state.value = state.value.copy(attachment = attachment)
        onNodeWithText("Чтение: 12 мс", substring = true).performScrollTo().assertExists()

        state.value = state.value.copy(attachment = AttachmentUi.Failed("Файл недоступен"))
        onNodeWithText("Файл недоступен").performScrollTo().assertExists()
    }

    @Test
    fun location_states_are_rendered() = runComposeUiTest {
        val state = androidx.compose.runtime.mutableStateOf(CapabilitiesState(platform = platform, location = LocationUi.Requesting))
        setContent { Screen(state.value) }

        onNodeWithText("Определяем…").performScrollTo().assertExists()

        state.value = state.value.copy(location = LocationUi.Located(GeoPoint(55.7558, 37.6173, 25.0), elapsedMs = 40))
        onNodeWithText("±25 м", substring = true).performScrollTo().assertExists()

        state.value = state.value.copy(location = LocationUi.Failed("Доступ к геопозиции запрещён"))
        onNodeWithText("Доступ к геопозиции запрещён").performScrollTo().assertExists()
    }
}
