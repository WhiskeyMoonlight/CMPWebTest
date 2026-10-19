package org.dimas.medextest.feature.marketplace.presentation.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dimas.medextest.core.common.LoadState
import org.dimas.medextest.core.designsystem.component.EmptyBlock
import org.dimas.medextest.core.designsystem.component.ErrorBlock
import org.dimas.medextest.core.designsystem.component.SectionHeader
import org.dimas.medextest.feature.marketplace.presentation.mvi.MarketplaceActions
import org.dimas.medextest.feature.marketplace.presentation.mvi.SectionState

@Composable
internal fun MarketplaceSectionCard(
    section: SectionState,
    actions: MarketplaceActions,
    modifier: Modifier = Modifier,
) {
    FullBleedCard(modifier) {
        Spacer(Modifier.height(16.dp))
        SectionHeader(title = section.id.title, onMoreClick = { actions.onSectionMoreClick(section.id) })
        Spacer(Modifier.height(12.dp))
        when (val load = section.load) {
            LoadState.Loading -> SectionSkeleton(section.id)
            LoadState.Empty -> EmptyBlock("Пока здесь пусто")
            is LoadState.Error -> ErrorBlock(load.error, onRetry = { actions.onRetrySection(section.id) })
            is LoadState.Content -> SectionContent(load.data, actions)
        }
        Spacer(Modifier.height(16.dp))
    }
}
