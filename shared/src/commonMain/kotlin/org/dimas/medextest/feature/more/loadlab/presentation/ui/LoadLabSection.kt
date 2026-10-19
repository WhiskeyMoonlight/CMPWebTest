package org.dimas.medextest.feature.more.loadlab.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.more.devsettings.presentation.ui.SettingsCard
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScenario
import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabActions
import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabState

@Composable
fun LoadLabSection(
    state: LoadLabState,
    actions: LoadLabActions,
    onPickFile: () -> Unit,
    onCopyReport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        SettingsCard(title = "Нагрузка: корутины и потоки") {
            Text(
                "${state.platformName} · ядер: ${state.cpuCores} · воркеров: ${state.workerPoolSize}",
                style = MaterialTheme.typography.bodySmall,
                color = MedExColors.InkSecondary,
            )
            Caption("Где считать")
            ModeSelector(modes = state.availableModes, selected = state.mode, onSelect = actions::onModeSelect)
            Caption("Объём")
            ScaleSelector(selected = state.scale, onSelect = actions::onScaleSelect)
            LoadScenario.entries.forEach { scenario ->
                ScenarioButton(
                    title = scenario.title,
                    description = scenario.description,
                    onClick = { actions.onScenarioClick(scenario) },
                    modifier = Modifier.testTag(LoadLabTags.scenario(scenario)),
                )
            }
            ScenarioButton(
                title = "SHA-256 файла…",
                description = "Системный выбор файла, чтение и хэш в выбранном режиме",
                onClick = onPickFile,
                modifier = Modifier.testTag(LoadLabTags.PICK_FILE),
            )
        }
        TaskListCard(
            tasks = state.tasks,
            hasRunningTasks = state.hasRunningTasks,
            hasFinishedTasks = state.hasFinishedTasks,
            onCancel = actions::onCancelClick,
            onCancelAll = actions::onCancelAllClick,
            onClearFinished = actions::onClearFinishedClick,
            onCopyReport = onCopyReport,
        )
    }
}

@Composable
private fun Caption(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelMedium,
        color = MedExColors.InkSecondary,
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
    )
}
