package org.dimas.medextest.feature.checkin.presentation.util

import org.dimas.medextest.core.designsystem.illustration.MoodFace
import org.dimas.medextest.feature.checkin.domain.model.Mood

fun Mood.face(): MoodFace = when (this) {
    Mood.Awful -> MoodFace.Star
    Mood.Bad -> MoodFace.Flower
    Mood.Neutral -> MoodFace.Sun
    Mood.Good -> MoodFace.Square
    Mood.Great -> MoodFace.Clover
}
