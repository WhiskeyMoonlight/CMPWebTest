package org.dimas.medextest.feature.more.devsettings.di

import org.dimas.medextest.core.mock.MockBackendConfigSource
import org.dimas.medextest.feature.more.devsettings.data.DevSettingsRepository
import org.dimas.medextest.feature.more.devsettings.presentation.DevSettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val devSettingsModule = module {
    singleOf(::DevSettingsRepository) bind MockBackendConfigSource::class
    viewModelOf(::DevSettingsViewModel)
}
