package org.dimas.medextest.feature.more.loadlab.di

import org.dimas.medextest.feature.more.loadlab.data.MockLabRepository
import org.dimas.medextest.feature.more.loadlab.data.MockSyncRepository
import org.dimas.medextest.feature.more.loadlab.domain.repository.LabRepository
import org.dimas.medextest.feature.more.loadlab.domain.repository.SyncRepository
import org.dimas.medextest.feature.more.loadlab.domain.usecase.HashPickedFileUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunAttachmentHashUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunLabImportUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunRequestStormUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunScanFilterUseCase
import org.dimas.medextest.feature.more.loadlab.domain.usecase.RunScenarioUseCase
import org.dimas.medextest.feature.more.loadlab.presentation.LoadLabViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val loadLabModule = module {
    singleOf(::MockLabRepository) bind LabRepository::class
    singleOf(::MockSyncRepository) bind SyncRepository::class
    factory { RunLabImportUseCase(get(), get()) }
    factory { RunAttachmentHashUseCase(get()) }
    factory { RunScanFilterUseCase(get()) }
    factory { RunRequestStormUseCase(get(), get()) }
    factory { HashPickedFileUseCase(get()) }
    factoryOf(::RunScenarioUseCase)
    viewModelOf(::LoadLabViewModel)
}
