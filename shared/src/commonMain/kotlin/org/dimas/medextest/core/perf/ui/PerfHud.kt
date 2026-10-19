package org.dimas.medextest.core.perf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.perf.PerfSnapshot

const val PERF_HUD_TAG = "perfHud"

private val HudBackground = Color(0xCC111418)
private val HudGood = Color(0xFF7BE495)
private val HudWarning = Color(0xFFFFD166)
private val HudBad = Color(0xFFFF6B6B)
private val HudText = Color(0xFFE8EDF2)
private const val SMOOTH_FRAME_MS = 20
private const val NOTICEABLE_FRAME_MS = 50
private const val PERCENT = 100

@Composable
fun PerfHud(
    snapshot: PerfSnapshot,
    running: RunningTasksSummary?,
    modifier: Modifier = Modifier,
) {
    val style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace)
    Column(
        modifier = modifier
            .testTag(PERF_HUD_TAG)
            .background(HudBackground, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = "${snapshot.fps} к/с · кадр ${snapshot.worstFrameMs} мс",
            style = style,
            color = frameColor(snapshot.worstFrameMs),
        )
        Text(text = "лаг ${snapshot.lagMs} мс", style = style, color = frameColor(snapshot.lagMs))
        if (running != null) {
            Text(
                text = "задач: ${running.count} · ${(running.progress * PERCENT).toInt()}%",
                style = style,
                color = HudText,
            )
        }
    }
}

private fun frameColor(millis: Int): Color = when {
    millis <= SMOOTH_FRAME_MS -> HudGood
    millis <= NOTICEABLE_FRAME_MS -> HudWarning
    else -> HudBad
}
