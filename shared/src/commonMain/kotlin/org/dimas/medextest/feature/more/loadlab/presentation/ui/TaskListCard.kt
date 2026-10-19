package org.dimas.medextest.feature.more.loadlab.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.OutlinedPillButton
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.more.devsettings.presentation.ui.SettingsCard
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUi

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TaskListCard(
    tasks: List<TaskUi>,
    hasRunningTasks: Boolean,
    hasFinishedTasks: Boolean,
    onCancel: (Int) -> Unit,
    onCancelAll: () -> Unit,
    onClearFinished: () -> Unit,
    onCopyReport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsCard(title = "Задачи", modifier = modifier) {
        if (tasks.isEmpty()) {
            Text(
                "Запустите сценарий и походите по приложению — задачи живут в scope приложения, а HUD покажет кадры.",
                style = MaterialTheme.typography.bodySmall,
                color = MedExColors.InkSecondary,
            )
        }
        tasks.forEachIndexed { index, task ->
            if (index > 0) HorizontalDivider(color = MedExColors.Divider)
            TaskRow(task = task, onCancel = { onCancel(task.id) })
        }
        if (tasks.isNotEmpty()) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (hasRunningTasks) OutlinedPillButton("Отменить все", onCancelAll, Modifier.testTag(LoadLabTags.CANCEL_ALL))
                if (hasFinishedTasks) OutlinedPillButton("Очистить", onClearFinished, Modifier.testTag(LoadLabTags.CLEAR))
                OutlinedPillButton("Скопировать отчёт", onCopyReport, Modifier.testTag(LoadLabTags.COPY_REPORT))
            }
        }
    }
}
