package org.dimas.medextest.core.location

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.dimas.medextest.testutil.geolocationCalls
import org.dimas.medextest.testutil.installGeolocationStub
import org.dimas.medextest.testutil.installPermissionsStub
import org.dimas.medextest.testutil.realDelay
import org.dimas.medextest.testutil.removeGeolocationStub
import org.dimas.medextest.testutil.removePermissionsStub
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class BrowserLocationServiceTest {
    private val service = BrowserLocationService()

    @BeforeTest
    fun hidePermissionsApi() = installPermissionsStub("missing")

    @AfterTest
    fun restoreBrowserApis() {
        removeGeolocationStub()
        removePermissionsStub()
    }

    private fun stub(mode: String) =
        installGeolocationStub(mode, latitude = 55.7558, longitude = 37.6173, accuracy = 25.0, delayMs = 20)

    @Test
    fun successful_fix_is_mapped_and_permission_granted() = runTest {
        stub("success")

        val point = withContext(Dispatchers.Default) { service.currentLocation(2.seconds) }

        assertEquals(GeoPoint(latitude = 55.7558, longitude = 37.6173, accuracyMeters = 25.0), point)
        assertEquals(LocationPermission.Granted, service.permissionStatus())
    }

    @Test
    fun denied_prompt_becomes_permission_error() = runTest {
        stub("denied")

        withContext(Dispatchers.Default) {
            assertFailsWith<LocationException.PermissionDenied> { service.currentLocation(2.seconds) }
        }
        assertEquals(LocationPermission.Denied, service.requestPermission())
    }

    @Test
    fun permission_follows_browser_settings_after_denial() = runTest {
        stub("denied")
        withContext(Dispatchers.Default) {
            assertFailsWith<LocationException.PermissionDenied> { service.currentLocation(2.seconds) }
        }

        installPermissionsStub("granted")
        assertEquals(LocationPermission.Granted, service.requestPermission())

        installPermissionsStub("prompt")
        assertEquals(LocationPermission.NotDetermined, service.requestPermission())

        installPermissionsStub("denied")
        assertEquals(LocationPermission.Denied, service.permissionStatus())
    }

    @Test
    fun missing_fix_becomes_unavailable_error() = runTest {
        stub("unavailable")

        withContext(Dispatchers.Default) {
            assertFailsWith<LocationException.Unavailable> { service.currentLocation(2.seconds) }
        }
        assertEquals(LocationPermission.NotDetermined, service.permissionStatus())
    }

    @Test
    fun browser_timeout_and_silent_browser_both_become_timeout() = runTest {
        stub("timeout")
        withContext(Dispatchers.Default) {
            assertFailsWith<LocationException.Timeout> { service.currentLocation(2.seconds) }
        }

        stub("silent")
        withContext(Dispatchers.Default) {
            assertFailsWith<LocationException.Timeout> { service.currentLocation(150.milliseconds) }
        }
    }

    @Test
    fun cancelled_request_ignores_late_answer() = runTest {
        stub("success")

        withContext(Dispatchers.Default) {
            val request = launch(start = CoroutineStart.UNDISPATCHED) { service.currentLocation(2.seconds) }
            request.cancel()
            realDelay(60.milliseconds)
        }

        assertEquals(1, geolocationCalls())
        assertEquals(LocationPermission.Granted, service.permissionStatus())
    }
}
