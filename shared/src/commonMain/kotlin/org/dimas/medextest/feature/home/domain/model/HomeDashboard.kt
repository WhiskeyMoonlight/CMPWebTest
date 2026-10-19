package org.dimas.medextest.feature.home.domain.model

data class HomeDashboard(
    val quickActions: List<QuickAction>,
    val clinicPhone: String,
    val showHealthProfilePromo: Boolean,
)
