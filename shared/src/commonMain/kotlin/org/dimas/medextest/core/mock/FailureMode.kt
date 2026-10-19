package org.dimas.medextest.core.mock

enum class FailureMode(val probability: Double) {
    Never(0.0),
    Sometimes(0.3),
    Always(1.0),
}
