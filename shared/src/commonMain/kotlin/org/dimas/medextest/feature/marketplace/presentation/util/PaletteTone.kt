package org.dimas.medextest.feature.marketplace.presentation.util

import org.dimas.medextest.core.designsystem.illustration.ArtTone
import org.dimas.medextest.feature.marketplace.domain.model.Palette

internal fun Palette.tone(): ArtTone = when (this) {
    Palette.Peach -> ArtTone.Peach
    Palette.Pink -> ArtTone.Pink
    Palette.Mint -> ArtTone.Mint
    Palette.Sky -> ArtTone.Sky
    Palette.Lavender -> ArtTone.Lavender
}
