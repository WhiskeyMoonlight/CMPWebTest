package org.dimas.medextest.feature.more.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.illustration.headerGradientBackground
import org.dimas.medextest.feature.profile.domain.model.Profile

@Composable
internal fun MoreHeader(
    profile: Profile?,
    contentMaxWidth: Dp,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth().headerGradientBackground().statusBarsPadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(Modifier.widthIn(max = contentMaxWidth).fillMaxWidth().padding(16.dp)) {
            MoreProfileRow(profile = profile)
            Spacer(Modifier.padding(top = 8.dp))
            content()
        }
    }
}
