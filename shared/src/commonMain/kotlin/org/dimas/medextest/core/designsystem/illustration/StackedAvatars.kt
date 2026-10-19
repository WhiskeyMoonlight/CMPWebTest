package org.dimas.medextest.core.designsystem.illustration

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StackedAvatars(
    seeds: List<Int>,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    overlap: Dp = 10.dp,
) {
    val step = size - overlap
    Box(modifier = modifier.width(step * (seeds.size - 1) + size)) {
        seeds.forEachIndexed { index, seed ->
            Avatar(
                initials = "",
                seed = seed,
                size = size,
                modifier = Modifier
                    .offset(x = step * index)
                    .border(2.dp, Color.White, CircleShape),
            )
        }
    }
}
