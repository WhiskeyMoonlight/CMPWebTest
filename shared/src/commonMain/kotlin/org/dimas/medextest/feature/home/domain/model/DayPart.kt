package org.dimas.medextest.feature.home.domain.model

enum class DayPart {
    Night,
    Morning,
    Day,
    Evening;

    companion object {
        fun of(hour: Int): DayPart = when (hour) {
            in 5..11 -> Morning
            in 12..17 -> Day
            in 18..22 -> Evening
            else -> Night
        }
    }
}
