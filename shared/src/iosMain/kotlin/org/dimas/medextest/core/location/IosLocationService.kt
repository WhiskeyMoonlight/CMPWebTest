package org.dimas.medextest.core.location

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLLocationAccuracyHundredMeters
import kotlin.coroutines.resume
import kotlin.time.Duration

class IosLocationService : LocationService {
    private val delegate = IosLocationDelegate()
    private val manager: CLLocationManager by lazy {
        CLLocationManager().also { manager ->
            manager.delegate = delegate
            manager.desiredAccuracy = kCLLocationAccuracyHundredMeters
        }
    }

    override suspend fun permissionStatus(): LocationPermission = withContext(Dispatchers.Main) {
        manager.authorizationStatus.toPermission()
    }

    override suspend fun requestPermission(): LocationPermission = withContext(Dispatchers.Main) {
        val current = manager.authorizationStatus
        if (current != kCLAuthorizationStatusNotDetermined) return@withContext current.toPermission()
        suspendCancellableCoroutine { continuation ->
            delegate.onAuthorizationChanged = { status ->
                if (status != kCLAuthorizationStatusNotDetermined) {
                    delegate.onAuthorizationChanged = null
                    if (continuation.isActive) continuation.resume(status.toPermission())
                }
            }
            continuation.invokeOnCancellation { delegate.onAuthorizationChanged = null }
            manager.requestWhenInUseAuthorization()
        }
    }

    override suspend fun currentLocation(timeout: Duration): GeoPoint = withContext(Dispatchers.Main) {
        if (manager.authorizationStatus.toPermission() != LocationPermission.Granted) {
            throw LocationException.PermissionDenied()
        }
        withTimeoutOrNull(timeout) {
            suspendCancellableCoroutine { continuation ->
                delegate.onLocationResult = { result ->
                    delegate.onLocationResult = null
                    if (continuation.isActive) continuation.resumeWith(result)
                }
                continuation.invokeOnCancellation {
                    delegate.onLocationResult = null
                    manager.stopUpdatingLocation()
                }
                manager.requestLocation()
            }
        } ?: throw LocationException.Timeout()
    }

    private fun CLAuthorizationStatus.toPermission(): LocationPermission = when (this) {
        kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorizedAlways -> LocationPermission.Granted
        kCLAuthorizationStatusNotDetermined -> LocationPermission.NotDetermined
        else -> LocationPermission.Denied
    }
}
