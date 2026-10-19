package org.dimas.medextest.feature.more.loadlab.presentation.util

import org.dimas.medextest.feature.more.loadlab.domain.model.LoadScale

internal val LoadScale.label: String get() = "×$factor"
