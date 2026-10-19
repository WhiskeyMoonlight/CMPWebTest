package org.dimas.medextest.core.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.CancellationSignal
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import org.dimas.medextest.core.platform.AndroidPermissionRequester
import org.dimas.medextest.core.storage.KeyValueStorage
import org.dimas.medextest.core.storage.StorageKeys
import org.dimas.medextest.core.storage.getBoolean
import org.dimas.medextest.core.storage.putBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.time.Duration

class AndroidLocationService(
    private val context: Context,
    private val permissionRequester: AndroidPermissionRequester,
    private val storage: KeyValueStorage,
) : LocationService {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override suspend fun permissionStatus(): LocationPermission = when {
        hasPermission() -> LocationPermission.Granted
        storage.getBoolean(StorageKeys.LOCATION_PERMISSION_ASKED) -> LocationPermission.Denied
        else -> LocationPermission.NotDetermined
    }

    override suspend fun requestPermission(): LocationPermission {
        if (hasPermission()) return LocationPermission.Granted
        val result = permissionRequester.request(LOCATION_PERMISSIONS)
        storage.putBoolean(StorageKeys.LOCATION_PERMISSION_ASKED, true)
        return if (result.values.any { granted -> granted }) LocationPermission.Granted else LocationPermission.Denied
    }

    override suspend fun currentLocation(timeout: Duration): GeoPoint {
        if (!hasPermission()) throw LocationException.PermissionDenied()
        if (!LocationManagerCompat.isLocationEnabled(locationManager)) throw LocationException.Disabled()
        val provider = PROVIDERS.firstOrNull { provider ->
            LocationManagerCompat.hasProvider(locationManager, provider) && locationManager.isProviderEnabled(provider)
        } ?: throw LocationException.Unavailable("no enabled location provider")
        return withTimeoutOrNull(timeout) { requestSingleFix(provider) } ?: throw LocationException.Timeout()
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestSingleFix(provider: String): GeoPoint = suspendCancellableCoroutine { continuation ->
        val cancellationSignal = CancellationSignal()
        continuation.invokeOnCancellation { cancellationSignal.cancel() }
        LocationManagerCompat.getCurrentLocation(
            locationManager,
            provider,
            cancellationSignal,
            ContextCompat.getMainExecutor(context),
        ) { location: Location? ->
            if (location != null) {
                continuation.resume(location.toGeoPoint())
            } else {
                continuation.resumeWithException(LocationException.Unavailable("provider $provider returned no fix"))
            }
        }
    }

    private fun hasPermission(): Boolean = LOCATION_PERMISSIONS.any { permission ->
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun Location.toGeoPoint() = GeoPoint(
        latitude = latitude,
        longitude = longitude,
        accuracyMeters = if (hasAccuracy()) accuracy.toDouble() else null,
    )

    private companion object {
        val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        val PROVIDERS = listOf(
            LocationManager.FUSED_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.GPS_PROVIDER,
        )
    }
}
