package org.dimas.medextest.feature.more.capabilities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.dimas.medextest.core.location.GeoPoint
import org.dimas.medextest.core.location.LocationPermission
import org.dimas.medextest.core.platform.PlatformInfo
import org.dimas.medextest.core.platform.PlatformKind
import org.dimas.medextest.feature.more.capabilities.presentation.CapabilitiesViewModel
import org.dimas.medextest.feature.more.capabilities.presentation.model.LocationUi
import org.dimas.medextest.testutil.MainDispatcherTest
import org.orbitmvi.orbit.test.testWithInternalState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CapabilitiesViewModelTest : MainDispatcherTest() {
    private fun viewModel(locationService: FakeLocationService) = CapabilitiesViewModel(
        locationService = locationService,
        defaultDispatcher = Dispatchers.Unconfined,
        platformInfo = PlatformInfo(name = "Test", kind = PlatformKind.Android),
    )

    @Test
    fun denied_permission_ends_with_failed_location() = runTest {
        val locationService = FakeLocationService(permission = LocationPermission.Denied)

        viewModel(locationService).testWithInternalState(this) {
            containerHost.onLocateClick()
            expectInternalState { copy(location = LocationUi.Requesting) }
            expectInternalState {
                copy(location = LocationUi.Failed("Доступ к геопозиции запрещён. Разрешите его в настройках"))
            }
        }
        assertEquals(0, locationService.locationRequests)
    }

    @Test
    fun granted_permission_ends_with_located_point() = runTest {
        val point = GeoPoint(latitude = 59.9343, longitude = 30.3351, accuracyMeters = 5.0)
        val locationService = FakeLocationService(location = point)

        viewModel(locationService).testWithInternalState(this) {
            containerHost.onLocateClick()
            expectInternalState { copy(location = LocationUi.Requesting) }
            val located = assertIs<LocationUi.Located>(awaitInternalState().location)
            assertEquals(point, located.point)
        }
        assertEquals(1, locationService.locationRequests)
    }
}
