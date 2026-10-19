package org.dimas.medextest.feature.more.loadlab.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUi
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUiStatus

private const val PERCENT = 100

@Composable
internal fun TaskRow(
    task: TaskUi,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth().testTag(LoadLabTags.task(task.id)).padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(task.title, style = MaterialTheme.typography.titleSmall, color = MedExColors.Ink, modifier = Modifier.weight(1f))
            Text(task.status.label, style = MaterialTheme.typography.labelMedium, color = task.status.color)
        }
        Text("${task.details} · ${task.elapsed}", style = MaterialTheme.typography.bodySmall, color = MedExColors.InkSecondary)
        if (task.status == TaskUiStatus.Running) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { task.progress },
                    modifier = Modifier.weight(1f),
                    color = MedExColors.Teal,
                    trackColor = MedExColors.CyanSoft,
                )
                Text(
                    "${(task.progress * PERCENT).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 8.dp),
                )
                TextButton(onClick = onCancel, modifier = Modifier.testTag(LoadLabTags.cancel(task.id))) { Text("Отменить") }
            }
        }
        task.result?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MedExColors.Ink) }
        task.perf?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MedExColors.InkSecondary) }
    }
}

private val TaskUiStatus.color: Color
    get() = when (this) {
        TaskUiStatus.Running -> MedExColors.Teal
        TaskUiStatus.Completed -> MedExColors.Green
        TaskUiStatus.Failed -> MedExColors.Red
        TaskUiStatus.Cancelled -> MedExColors.InkSecondary
    }
