package org.dimas.medextest.ui

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.v2.runComposeUiTest
import org.dimas.medextest.app.MedExApp
import org.dimas.medextest.app.di.appModules
import org.dimas.medextest.core.background.BackgroundTasks
import org.dimas.medextest.core.background.TaskStatus
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.core.storage.KeyValueStorage
import org.dimas.medextest.core.storage.StorageKeys
import org.dimas.medextest.core.storage.putBoolean
import org.dimas.medextest.core.storage.putLocalDate
import org.dimas.medextest.core.time.TimeProvider
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScenario
import org.dimas.medextest.feature.more.loadlab.domain.repository.SyncRepository
import org.dimas.medextest.feature.more.loadlab.presentation.ui.LoadLabTags
import org.dimas.medextest.testutil.FakeSyncRepository
import org.dimas.medextest.testutil.awaitCondition
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatform
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalTestApi::class)
class AppUnderLoadUiTest {
    private val fastSync = module { single<SyncRepository> { FakeSyncRepository(latency = 2.milliseconds) } }

    @BeforeTest
    fun startApp() {
        if (KoinPlatform.getKoinOrNull() != null) stopKoin()
        val koin = startKoin { modules(appModules + fastSync) }.koin
        val storage = koin.get<KeyValueStorage>()
        storage.putBoolean(StorageKeys.ONBOARDING_COMPLETED, true)
        storage.putLocalDate(StorageKeys.CHECK_IN_LAST_PROMPTED, TimeProvider().today())
    }

    @AfterTest
    fun stopApp() {
        KoinPlatform.getKoin().get<BackgroundTasks>().cancelAll()
        stopKoin()
    }

    private fun ComposeUiTest.tab(label: String) =
        onNode(hasText(label) and SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Tab))

    private fun ComposeUiTest.isTextShown(text: String): Boolean =
        onAllNodesWithText(text, substring = true).fetchSemanticsNodes().isNotEmpty()

    @Test
    fun navigation_keeps_working_while_background_scenario_runs() = runComposeUiTest {
        val backgroundTasks = KoinPlatform.getKoin().get<BackgroundTasks>()
        setContent { MedExApp(measurePerformance = false) }

        tab("Ещё").performClick()
        onNodeWithText("Для разработчика").performScrollTo().performClick()
        onNodeWithTag(LoadLabTags.mode(ExecutionMode.Cooperative)).performScrollTo().performClick()
        onNodeWithTag(LoadLabTags.scenario(LoadScenario.RequestStorm)).performScrollTo().performClick()
        awaitCondition("задача запущена") { backgroundTasks.tasks.value.isNotEmpty() }

        tab("Главная").performClick()
        awaitCondition("Главная отрисована") { isTextShown("Активности на сегодня") }
        tab("Витрина").performClick()
        awaitCondition("Витрина загрузила акции", timeout = 20.seconds) { isTextShown("Акции") }

        awaitCondition("сценарий завершён", timeout = 30.seconds) {
            backgroundTasks.tasks.value.single().status != TaskStatus.Running
        }
        val finished = backgroundTasks.tasks.value.single()
        assertTrue(finished.status is TaskStatus.Completed, finished.toString())

        tab("Ещё").performClick()
        awaitCondition("результат виден на экране разработчика") { isTextShown("10000 запросов") }
    }

    @Test
    fun task_survives_leaving_dev_screen_and_is_cancelled_from_new_screen_instance() = runComposeUiTest {
        val backgroundTasks = KoinPlatform.getKoin().get<BackgroundTasks>()
        setContent { MedExApp(measurePerformance = false) }

        tab("Ещё").performClick()
        onNodeWithText("Для разработчика").performScrollTo().performClick()
        onNodeWithTag(LoadLabTags.mode(ExecutionMode.Cooperative)).performScrollTo().performClick()
        onNodeWithText("×10").performScrollTo().performClick()
        onNodeWithTag(LoadLabTags.scenario(LoadScenario.LabImport)).performScrollTo().performClick()
        awaitCondition("импорт запущен") { backgroundTasks.tasks.value.isNotEmpty() }
        val taskId = backgroundTasks.tasks.value.single().id

        onNodeWithContentDescription("Назад").performScrollTo().performClick()
        awaitCondition("вернулись в «Ещё»") { !isTextShown("Нагрузка: корутины и потоки") }
        assertEquals(TaskStatus.Running, backgroundTasks.tasks.value.single().status)

        onNodeWithText("Для разработчика").performScrollTo().performClick()
        awaitCondition("новый экран подхватил запущенную задачу") {
            onAllNodesWithTag(LoadLabTags.cancel(taskId)).fetchSemanticsNodes().isNotEmpty()
        }
        onNodeWithTag(LoadLabTags.cancel(taskId)).performScrollTo().performClick()
        awaitCondition("задача отменена") { backgroundTasks.tasks.value.single().status == TaskStatus.Cancelled }
        awaitCondition("статус «Отменено» на экране") { isTextShown("Отменено") }
    }
}
