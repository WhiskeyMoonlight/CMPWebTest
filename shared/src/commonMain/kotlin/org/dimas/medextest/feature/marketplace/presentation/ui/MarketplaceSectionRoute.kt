package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.designsystem.adaptive.LocalWindowWidthClass
import org.dimas.medextest.core.designsystem.adaptive.contentMaxWidth
import org.dimas.medextest.core.designsystem.component.BackTopBar
import org.dimas.medextest.core.designsystem.component.EmptyBlock
import org.dimas.medextest.core.designsystem.theme.MedExColors
import org.dimas.medextest.feature.marketplace.domain.model.MarketplaceSectionId

@Composable
fun MarketplaceSectionRoute(sectionSlug: String, onBack: () -> Unit) {
    val section = MarketplaceSectionId.fromSlug(sectionSlug)
    val widthClass = LocalWindowWidthClass.current
    Column(
        modifier = Modifier.fillMaxSize().background(MedExColors.Background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackTopBar(title = section?.title ?: "Раздел", onBack = onBack)
        Spacer(Modifier.height(24.dp))
        EmptyBlock(
            message = if (section == null) {
                "Раздел не найден"
            } else {
                "Полный каталог раздела «${section.title}» появится в следующих версиях"
            },
            modifier = Modifier.widthIn(max = widthClass.contentMaxWidth),
        )
    }
}
