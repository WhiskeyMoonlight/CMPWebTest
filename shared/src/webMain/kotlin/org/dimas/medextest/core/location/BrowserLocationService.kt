package org.dimas.medextest.core.location

import js.objects.unsafeJso
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import org.dimas.medextest.core.common.runCatchingCancellable
import web.geolocation.GeolocationCoordinates
import web.geolocation.GeolocationPositionError
import web.geolocation.PositionOptions
import web.navigator.navigator
import web.permissions.PermissionDescriptor
import web.permissions.PermissionName
import web.permissions.geolocation
import web.permissions.query
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.time.Duration

class BrowserLocationService : LocationService {
    private var lastKnownPermission = LocationPermission.NotDetermined

    override suspend fun permissionStatus(): LocationPermission = queryPermission() ?: lastKnownPermission

    override suspend fun requestPermission(): LocationPermission = permissionStatus()

    override suspend fun currentLocation(timeout: Duration): GeoPoint =
        withTimeoutOrNull(timeout) { requestPosition() } ?: throw LocationException.Timeout()

    private suspend fun requestPosition(): GeoPoint = suspendCancellableCoroutine { continuation ->
        val options = unsafeJso<PositionOptions> {
            enableHighAccuracy = true
            maximumAge = MAX_CACHED_POSITION_AGE_MS
        }
        navigator.geolocation.getCurrentPositionWithCallbacks(
            successCallback = { position ->
                lastKnownPermission = LocationPermission.Granted
                if (continuation.isActive) continuation.resume(position.coords.toGeoPoint())
            },
            errorCallback = { error ->
                val exception = error.toLocationException()
                if (exception is LocationException.PermissionDenied) lastKnownPermission = LocationPermission.Denied
                if (continuation.isActive) continuation.resumeWithException(exception)
            },
            options = options,
        )
    }

    private suspend fun queryPermission(): LocationPermission? = runCatchingCancellable {
        val status = navigator.permissions.query(unsafeJso<PermissionDescriptor> { name = PermissionName.geolocation })
        when (status.state.toString()) {
            GRANTED -> LocationPermission.Granted
            DENIED -> LocationPermission.Denied
            else -> LocationPermission.NotDetermined
        }
    }.getOrNull()

    private fun GeolocationCoordinates.toGeoPoint() = GeoPoint(
        latitude = latitude,
        longitude = longitude,
        accuracyMeters = accuracy,
    )

    private fun GeolocationPositionError.toLocationException(): LocationException = when (code) {
        GeolocationPositionError.PERMISSION_DENIED -> LocationException.PermissionDenied()
        GeolocationPositionError.TIMEOUT -> LocationException.Timeout()
        else -> LocationException.Unavailable(message)
    }

    private companion object {
        const val MAX_CACHED_POSITION_AGE_MS = 60_000
        const val GRANTED = "granted"
        const val DENIED = "denied"
    }
}
