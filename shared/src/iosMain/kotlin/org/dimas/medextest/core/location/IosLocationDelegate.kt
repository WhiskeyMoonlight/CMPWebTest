package org.dimas.medextest.core.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSError
import platform.darwin.NSObject

private const val CL_ERROR_DENIED = 1L

internal class IosLocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {
    var onAuthorizationChanged: ((CLAuthorizationStatus) -> Unit)? = null
    var onLocationResult: ((Result<GeoPoint>) -> Unit)? = null

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        onAuthorizationChanged?.invoke(manager.authorizationStatus)
    }

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val location = didUpdateLocations.lastOrNull() as? CLLocation ?: return
        onLocationResult?.invoke(Result.success(location.toGeoPoint()))
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        val error = if (didFailWithError.code == CL_ERROR_DENIED) {
            LocationException.PermissionDenied()
        } else {
            LocationException.Unavailable(didFailWithError.localizedDescription)
        }
        onLocationResult?.invoke(Result.failure(error))
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun CLLocation.toGeoPoint(): GeoPoint = coordinate.useContents {
        GeoPoint(
            latitude = latitude,
            longitude = longitude,
            accuracyMeters = horizontalAccuracy.takeIf { it >= 0 },
        )
    }
}
