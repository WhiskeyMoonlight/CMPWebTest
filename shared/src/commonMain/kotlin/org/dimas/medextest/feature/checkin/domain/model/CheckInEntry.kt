package org.dimas.medextest.feature.checkin.domain.model

data class CheckInEntry(
    val mood: Mood?,
    val wellbeing: Int?,
    val symptomIds: Set<String>,
    val note: String,
) {
    val isEmpty: Boolean get() = mood == null && wellbeing == null && symptomIds.isEmpty() && note.isBlank()
}
