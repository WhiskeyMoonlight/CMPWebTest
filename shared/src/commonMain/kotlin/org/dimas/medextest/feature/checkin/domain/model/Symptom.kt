package org.dimas.medextest.feature.checkin.domain.model

data class Symptom(
    val id: String,
    val title: String,
    val tone: SymptomTone,
)
