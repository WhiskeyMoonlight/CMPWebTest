package org.dimas.medextest.feature.profile.di

import org.dimas.medextest.feature.profile.data.MockProfileRepository
import org.dimas.medextest.feature.profile.domain.repository.ProfileRepository
import org.dimas.medextest.feature.profile.domain.usecase.GetProfileUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val profileModule = module {
    single<ProfileRepository> { MockProfileRepository(get()) }
    factoryOf(::GetProfileUseCase)
}
