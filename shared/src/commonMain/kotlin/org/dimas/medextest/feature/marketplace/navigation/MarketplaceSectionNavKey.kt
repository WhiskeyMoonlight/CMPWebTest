package org.dimas.medextest.feature.marketplace.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class MarketplaceSectionNavKey(val sectionSlug: String) : NavKey
