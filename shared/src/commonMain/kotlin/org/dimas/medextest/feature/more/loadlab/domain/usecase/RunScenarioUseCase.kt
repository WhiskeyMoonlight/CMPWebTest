package org.dimas.medextest.feature.more.loadlab.domain.usecase

import org.dimas.medextest.core.background.TaskProgress
import org.dimas.medextest.core.compute.ExecutionMode
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale
import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScenario

class RunScenarioUseCase(
    private val labImport: RunLabImportUseCase,
    private val attachmentHash: RunAttachmentHashUseCase,
    private val scanFilter: RunScanFilterUseCase,
    private val requestStorm: RunRequestStormUseCase,
) {
    suspend operator fun invoke(scenario: LoadScenario, mode: ExecutionMode, scale: LoadScale, progress: TaskProgress): String =
        when (scenario) {
            LoadScenario.LabImport -> labImport(mode, scale, progress)
            LoadScenario.AttachmentHash -> attachmentHash(mode, scale, progress)
            LoadScenario.ScanFilter -> scanFilter(mode, scale, progress)
            LoadScenario.RequestStorm -> requestStorm(mode, scale, progress)
        }
}
