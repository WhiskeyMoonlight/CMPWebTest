package org.dimas.medextest.feature.marketplace.presentation.util

import org.dimas.medextest.core.format.formatPriceFrom
import org.dimas.medextest.feature.marketplace.domain.model.SearchResult

private const val SUBTITLE_SEPARATOR = " · "

internal fun SearchResult.subtitle(): String =
    listOfNotNull(section.title, priceFrom?.let(::formatPriceFrom)).joinToString(SUBTITLE_SEPARATOR)
