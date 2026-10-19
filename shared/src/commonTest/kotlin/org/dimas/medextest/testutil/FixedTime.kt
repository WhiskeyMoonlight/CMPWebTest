package org.dimas.medextest.testutil

import kotlinx.datetime.TimeZone
import org.dimas.medextest.core.time.TimeProvider
import kotlin.time.Instant

fun fixedTime(isoInstant: String): TimeProvider =
    TimeProvider(clock = FixedClock(Instant.parse(isoInstant)), timeZone = { TimeZone.UTC })
