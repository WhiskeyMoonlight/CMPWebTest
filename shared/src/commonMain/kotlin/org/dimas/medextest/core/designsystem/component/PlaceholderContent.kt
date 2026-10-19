package org.dimas.medextest.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.contentMaxWidth
import org.dimas.medextest.core.designsystem.icon.LogoMark
import org.dimas.medextest.core.designsystem.theme.MedExColors

@Composable
fun PlaceholderContent(
    title: String,
    description: String,
    onBack: (() -> Unit)? = null,
) {
    val contentMaxWidth = LocalWindowWidthClass.current.contentMaxWidth
    Column(Modifier.fillMaxSize().background(MedExColors.Background)) {
        if (onBack != null) BackTopBar(title = title, onBack = onBack) else CenteredTitleBar(title = title)
        Spacer(Modifier.weight(1f))
        Image(LogoMark, contentDescription = null, modifier = Modifier.size(96.dp).align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(16.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MedExColors.InkSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .widthIn(max = contentMaxWidth)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
        )
        Spacer(Modifier.weight(1.4f))
    }
}
