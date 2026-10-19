package org.dimas.medextest.app.di

import org.dimas.medextest.core.background.backgroundModule
import org.dimas.medextest.core.compute.computeModule
import org.dimas.medextest.core.compute.computePlatformModule
import org.dimas.medextest.core.coroutines.coroutinesModule
import org.dimas.medextest.core.location.locationModule
import org.dimas.medextest.core.mock.mockModule
import org.dimas.medextest.core.perf.perfModule
import org.dimas.medextest.core.platform.platformModule
import org.dimas.medextest.core.storage.storageModule
import org.dimas.medextest.core.time.timeModule
import org.dimas.medextest.feature.checkin.di.checkInModule
import org.dimas.medextest.feature.home.di.homeModule
import org.dimas.medextest.feature.marketplace.di.marketplaceModule
import org.dimas.medextest.feature.more.capabilities.di.capabilitiesModule
import org.dimas.medextest.feature.more.devsettings.di.devSettingsModule
import org.dimas.medextest.feature.more.di.moreModule
import org.dimas.medextest.feature.more.loadlab.di.loadLabModule
import org.dimas.medextest.feature.onboarding.di.onboardingModule
import org.dimas.medextest.feature.profile.di.profileModule
import org.koin.core.module.Module

internal val appModules: List<Module> = listOf(
    coroutinesModule,
    timeModule,
    storageModule,
    locationModule,
    platformModule,
    computePlatformModule,
    computeModule,
    perfModule,
    backgroundModule,
    mockModule,
    devSettingsModule,
    profileModule,
    onboardingModule,
    checkInModule,
    homeModule,
    marketplaceModule,
    moreModule,
    capabilitiesModule,
    loadLabModule,
)
