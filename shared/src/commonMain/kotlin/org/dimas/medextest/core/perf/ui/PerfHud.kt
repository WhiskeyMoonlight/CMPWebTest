package org.dimas.medextest.core.perf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.perf.PerfSnapshot

const val PERF_HUD_TAG = "perfHud"

private val HudBackground = Color(0xCC111418)
private val HudGood = Color(0xFF7BE495)
private val HudWarning = Color(0xFFFFD166)
private val HudBad = Color(0xFFFF6B6B)
private val HudText = Color(0xFFE8EDF2)
private val HudShape = RoundedCornerShape(10.dp)
private val HudPaddingHorizontal = 8.dp
private val HudPaddingVertical = 4.dp
private const val WIDEST_LINE = "000 к/с · кадр 0000 мс"
private const val IDLE_LINES = 2
private const val BUSY_LINES = 3

@Composable
fun PerfHud(
    snapshot: () -> PerfSnapshot,
    running: () -> RunningTasksSummary?,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()
    val busy = remember(running) { derivedStateOf { running() != null } }
    val style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace)
    Spacer(
        modifier = modifier
            .testTag(PERF_HUD_TAG)
            .clearAndSetSemantics {}
            .layout { measurable, _ ->
                val line = measurer.measure(WIDEST_LINE, style).size
                val lines = if (busy.value) BUSY_LINES else IDLE_LINES
                val width = line.width + 2 * HudPaddingHorizontal.roundToPx()
                val height = line.height * lines + 2 * HudPaddingVertical.roundToPx()
                val placeable = measurable.measure(Constraints.fixed(width, height))
                layout(width, height) { placeable.place(0, 0) }
            }
            .graphicsLayer()
            .background(HudBackground, HudShape)
            .drawBehind {
                val left = HudPaddingHorizontal.toPx()
                var top = HudPaddingVertical.toPx()
                perfHudLines(snapshot(), running()).forEach { line ->
                    val layout = measurer.measure(line.text, style)
                    drawText(layout, color = line.tone.color(), topLeft = Offset(left, top))
                    top += layout.size.height
                }
            },
    )
}

private fun HudTone.color(): Color = when (this) {
    HudTone.Good -> HudGood
    HudTone.Warning -> HudWarning
    HudTone.Bad -> HudBad
    HudTone.Neutral -> HudText
}
