package org.dimas.medextest.core.storage

object StorageKeys {
    private const val PREFIX = "medex."
    const val ONBOARDING_COMPLETED = "${PREFIX}onboarding.completed"
    const val CHECK_IN_LAST_PROMPTED = "${PREFIX}checkin.lastPrompted"
    const val CHECK_IN_LAST_SUBMITTED = "${PREFIX}checkin.lastSubmitted"
    const val DEV_LATENCY_MS = "${PREFIX}dev.latencyMs"
    const val DEV_FAILURE_MODE = "${PREFIX}dev.failureMode"
    const val DEV_FORCE_EMPTY = "${PREFIX}dev.forceEmpty"
    const val DEV_PERF_HUD = "${PREFIX}dev.perfHud"
    const val LOCATION_PERMISSION_ASKED = "${PREFIX}location.permissionAsked"
}
