package org.dimas.medextest.feature.home.domain.model

data class Greeting(
    val dayPart: DayPart,
    val firstName: String,
    val initials: String,
    val avatarSeed: Int,
)
