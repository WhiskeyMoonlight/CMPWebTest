package org.dimas.medextest.core.location

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface LocationService {
    suspend fun permissionStatus(): LocationPermission

    suspend fun requestPermission(): LocationPermission

    suspend fun currentLocation(timeout: Duration = DEFAULT_TIMEOUT): GeoPoint

    companion object {
        val DEFAULT_TIMEOUT: Duration = 20.seconds
    }
}
