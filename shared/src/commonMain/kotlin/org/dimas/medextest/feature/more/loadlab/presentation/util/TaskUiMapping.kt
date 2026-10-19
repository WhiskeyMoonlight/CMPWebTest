package org.dimas.medextest.feature.more.loadlab.presentation.util

import org.dimas.medextest.core.background.BackgroundTask
import org.dimas.medextest.core.background.TaskStatus
import org.dimas.medextest.core.format.formatDuration
import org.dimas.medextest.core.perf.FrameWindowStats
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUi
import org.dimas.medextest.feature.more.loadlab.presentation.model.TaskUiStatus

internal fun BackgroundTask.toUi(): TaskUi = TaskUi(
    id = id,
    title = title,
    details = details,
    status = when (status) {
        TaskStatus.Running -> TaskUiStatus.Running
        is TaskStatus.Completed -> TaskUiStatus.Completed
        is TaskStatus.Failed -> TaskUiStatus.Failed
        TaskStatus.Cancelled -> TaskUiStatus.Cancelled
    },
    progress = progress,
    elapsed = formatDuration(elapsed),
    result = when (val current = status) {
        is TaskStatus.Completed -> current.summary
        is TaskStatus.Failed -> current.reason
        TaskStatus.Running, TaskStatus.Cancelled -> null
    },
    perf = perf?.label(),
)

internal fun FrameWindowStats.label(): String =
    if (!isMeasured) {
        "Кадры не измерялись: UI не рисовал во время задачи"
    } else {
        "$averageFps к/с · худший кадр ${formatDuration(worstFrame)} · >17 мс: $slowFrames · >700 мс: $frozenFrames" +
            " · лаг UI до ${formatDuration(worstLag)}"
    }
