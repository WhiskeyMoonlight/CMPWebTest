package org.dimas.medextest.feature.more.capabilities.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.component.SurfaceCard
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.more.capabilities.presentation.model.AttachmentUi
import org.dimas.medextest.feature.more.capabilities.presentation.util.summary

@Composable
internal fun AttachmentResult(
    attachment: AttachmentUi,
    modifier: Modifier = Modifier,
) {
    val text = when (attachment) {
        AttachmentUi.Idle -> return
        is AttachmentUi.Reading -> "Читаем «${attachment.name}»…"
        is AttachmentUi.Failed -> attachment.reason
        is AttachmentUi.Ready -> attachment.summary()
    }
    SurfaceCard(modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(text, style = MaterialTheme.typography.bodyMedium, color = MedExColors.Ink)
            val preview = (attachment as? AttachmentUi.Ready)?.preview
            if (preview != null) {
                Spacer(Modifier.height(12.dp))
                Image(
                    bitmap = preview,
                    contentDescription = "Выбранное изображение",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().heightIn(max = 240.dp).clip(RoundedCornerShape(12.dp)),
                )
            }
        }
    }
}
