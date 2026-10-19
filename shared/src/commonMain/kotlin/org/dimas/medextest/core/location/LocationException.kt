package org.dimas.medextest.core.location

sealed class LocationException(message: String) : Exception(message) {
    class PermissionDenied : LocationException("Location permission denied")
    class Disabled : LocationException("Location services are disabled")
    class Timeout : LocationException("Location request timed out")
    class Unavailable(reason: String?) : LocationException("Location unavailable: $reason")
}
