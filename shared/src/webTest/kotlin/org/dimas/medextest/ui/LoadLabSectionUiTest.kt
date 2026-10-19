package org.dimas.medextest.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.v2.runComposeUiTest
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.designsystem.theme.MedExTheme
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScenario
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUi
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUiStatus
import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabState
import org.dimas.medextest.feature.more.loadlab.presentation.ui.LoadLabSection
import org.dimas.medextest.feature.more.loadlab.presentation.ui.LoadLabTags
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class LoadLabSectionUiTest {
    private val running = TaskUi(7, "Импорт анализов", "Нарезка · ×1", TaskUiStatus.Running, 0.42f, "1,20 с", null, null)
    private val finished = TaskUi(6, "SHA-256 вложений", "Web Worker · ×1", TaskUiStatus.Completed, 1f, "104 мс", "8 × 8 МБ", "60 к/с")

    private fun state(modes: List<ExecutionMode> = ExecutionMode.entries, tasks: List<TaskUi> = emptyList()) = LoadLabState(
        platformName = "Chrome · Kotlin/JS",
        cpuCores = 8,
        workerPoolSize = 7,
        availableModes = modes,
        tasks = tasks,
    )

    @Composable
    private fun Scrollable(content: @Composable () -> Unit) {
        MedExTheme { Column(Modifier.verticalScroll(rememberScrollState())) { content() } }
    }

    @Test
    fun mode_scale_and_scenarios_are_forwarded_to_actions() = runComposeUiTest {
        val actions = FakeLoadLabActions()
        var pickRequests = 0
        setContent {
            Scrollable { LoadLabSection(state(), actions, onPickFile = { pickRequests++ }, onCopyReport = {}) }
        }

        onNodeWithTag(LoadLabTags.mode(ExecutionMode.Cooperative)).performScrollTo().performClick()
        onNodeWithText("×4").performScrollTo().performClick()
        LoadScenario.entries.forEach { onNodeWithTag(LoadLabTags.scenario(it)).performScrollTo().performClick() }
        onNodeWithTag(LoadLabTags.PICK_FILE).performScrollTo().performClick()

        assertEquals(
            listOf("mode:Cooperative", "scale:X4") + LoadScenario.entries.map { "scenario:${it.name}" },
            actions.calls,
        )
        assertEquals(1, pickRequests)
    }

    @Test
    fun worker_chip_is_hidden_without_worker_pool() = runComposeUiTest {
        setContent {
            Scrollable {
                LoadLabSection(state(modes = ExecutionMode.entries - ExecutionMode.Worker), FakeLoadLabActions(), {}, {})
            }
        }

        onNodeWithTag(LoadLabTags.mode(ExecutionMode.Default)).assertExists()
        onNodeWithTag(LoadLabTags.mode(ExecutionMode.Worker)).assertDoesNotExist()
    }

    @Test
    fun running_task_shows_progress_and_can_be_cancelled() = runComposeUiTest {
        val actions = FakeLoadLabActions()
        var copied = 0
        setContent {
            Scrollable { LoadLabSection(state(tasks = listOf(running, finished)), actions, onPickFile = {}, onCopyReport = { copied++ }) }
        }

        onNodeWithText("42%").performScrollTo().assertExists()
        onNodeWithText("Нарезка · ×1 · 1,20 с").assertExists()
        onNodeWithText("8 × 8 МБ").assertExists()
        onNodeWithText("60 к/с").assertExists()
        onNodeWithTag(LoadLabTags.cancel(7)).performScrollTo().performClick()
        onNodeWithTag(LoadLabTags.cancel(6)).assertDoesNotExist()
        onNodeWithTag(LoadLabTags.CANCEL_ALL).performScrollTo().performClick()
        onNodeWithTag(LoadLabTags.CLEAR).performScrollTo().performClick()
        onNodeWithTag(LoadLabTags.COPY_REPORT).performScrollTo().performClick()

        assertEquals(listOf("cancel:7", "cancelAll", "clear"), actions.calls)
        assertEquals(1, copied)
    }

    @Test
    fun empty_task_list_explains_what_to_do() = runComposeUiTest {
        setContent { Scrollable { LoadLabSection(state(), FakeLoadLabActions(), {}, {}) } }

        onNodeWithText("Запустите сценарий", substring = true).performScrollTo().assertExists()
        onNodeWithTag(LoadLabTags.COPY_REPORT).assertDoesNotExist()
    }
}
