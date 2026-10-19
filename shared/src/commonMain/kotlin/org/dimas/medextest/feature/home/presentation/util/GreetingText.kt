package org.dimas.medextest.feature.home.presentation.util

import org.dimas.medextest.feature.home.domain.model.Greeting

internal fun Greeting.text(): String = "${dayPart.salutation()}, $firstName 👋"
