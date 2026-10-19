package org.dimas.medextest.feature.more.di

import org.dimas.medextest.feature.more.presentation.MoreViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val moreModule = module {
    viewModelOf(::MoreViewModel)
}
