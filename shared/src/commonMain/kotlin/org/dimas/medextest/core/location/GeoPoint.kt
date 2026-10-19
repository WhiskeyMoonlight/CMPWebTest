package org.dimas.medextest.core.location

data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Double?,
)
