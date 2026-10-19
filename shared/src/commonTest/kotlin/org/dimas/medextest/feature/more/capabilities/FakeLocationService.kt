package org.dimas.medextest.feature.more.capabilities

import org.dimas.medextest.core.location.GeoPoint
import org.dimas.medextest.core.location.LocationPermission
import org.dimas.medextest.core.location.LocationService
import kotlin.time.Duration

class FakeLocationService(
    private val permission: LocationPermission = LocationPermission.Granted,
    private val location: GeoPoint = GeoPoint(latitude = 55.7558, longitude = 37.6173, accuracyMeters = 12.0),
) : LocationService {
    var locationRequests = 0
        private set

    override suspend fun permissionStatus(): LocationPermission = permission

    override suspend fun requestPermission(): LocationPermission = permission

    override suspend fun currentLocation(timeout: Duration): GeoPoint {
        locationRequests++
        return location
    }
}
