package org.dimas.medextest.feature.more.capabilities.di

import org.dimas.medextest.core.coroutines.DispatcherQualifier
import org.dimas.medextest.feature.more.capabilities.presentation.CapabilitiesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val capabilitiesModule = module {
    viewModel {
        CapabilitiesViewModel(
            locationService = get(),
            defaultDispatcher = get(named(DispatcherQualifier.Default)),
            platformInfo = get(),
        )
    }
}
