package org.dimas.medextest.feature.more.loadlab.presentation.util

import org.dimas.medextest.feature.more.loadlab.presentation.mvi.LoadLabState

internal fun LoadLabState.toMarkdownReport(): String = buildString {
    appendLine("Платформа: $platformName · ядер: $cpuCores · воркеров: $workerPoolSize")
    appendLine()
    appendLine("| Сценарий | Режим | Статус | Время | Кадры | Результат |")
    appendLine("| --- | --- | --- | --- | --- | --- |")
    tasks.asReversed().forEach { task ->
        appendLine("| ${task.title} | ${task.details} | ${task.status.label} | ${task.elapsed} | ${task.perf.orEmpty()} | ${task.result.orEmpty()} |")
    }
}
