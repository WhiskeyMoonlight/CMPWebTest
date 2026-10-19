package org.dimas.medextest.feature.more.loadlab.debug

import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import org.dimas.medextest.core.background.BackgroundTask
import org.dimas.medextest.core.background.BackgroundTasks
import org.dimas.medextest.core.background.TaskStatus
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScenario
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunScenarioUseCase
import org.dimas.medextest.feature.more.loadlab.presentation.util.label

internal class LoadLabPerfBridge(
    private val backgroundTasks: BackgroundTasks,
    private val runScenario: RunScenarioUseCase,
) {
    fun run(spec: String): Int {
        val (scenarioName, modeName, scaleName) = spec.split(SEPARATOR)
        val scenario = LoadScenario.valueOf(scenarioName)
        val mode = ExecutionMode.valueOf(modeName)
        val scale = LoadScale.valueOf(scaleName)
        return backgroundTasks.launch(title = scenario.title, details = "${mode.label} · ${scale.label}") { progress ->
            runScenario(scenario, mode, scale, progress)
        }
    }

    fun clearFinished() = backgroundTasks.clearFinished()

    fun tasksJson(): String = buildJsonArray {
        backgroundTasks.tasks.value.forEach { task -> addJsonObject { describe(task) } }
    }.toString()

    private fun kotlinx.serialization.json.JsonObjectBuilder.describe(task: BackgroundTask) {
        put("id", task.id)
        put("title", task.title)
        put("details", task.details)
        put("status", task.status::class.simpleName)
        put("summary", (task.status as? TaskStatus.Completed)?.summary ?: (task.status as? TaskStatus.Failed)?.reason)
        put("progress", task.progress)
        put("elapsedMs", task.elapsed.inWholeMilliseconds)
        task.perf?.let { perf ->
            put("frames", perf.frames)
            put("fps", perf.averageFps)
            put("slowFrames", perf.slowFrames)
            put("frozenFrames", perf.frozenFrames)
            put("worstFrameMs", perf.worstFrame.inWholeMilliseconds)
            put("worstLagMs", perf.worstLag.inWholeMilliseconds)
        }
    }

    private companion object {
        const val SEPARATOR = ":"
    }
}
