package com.lucane.studio.flux.feature.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.ui.cards.CardWithHeader
import com.lucane.studio.flux.core.ui.cards.InternalCard
import com.lucane.studio.flux.core.ui.model.HeaderInfos
import com.lucane.studio.flux.core.R as CoreRes

/** One entry to display in [CyclePhaseSection]'s horizontal list. */
data class CyclePhaseEntry(
    val dateLabel: String,
    @StringRes val phaseLabel: Int,
    @DrawableRes val iconRes: Int,
)

/**
 * "Cycle phase" section — header row + horizontally scrollable list of
 * upcoming phase dates, reusing the shared [CardWithHeader]/[InternalCard].
 *
 * [entries] is example content for now: predicted dates already exist in
 * [com.lucane.studio.flux.feature.calendar.presentation.CalendarViewModel]
 * (next period, ovulation, fertile window) and could back this list directly —
 * left static pending a decision on wiring it up.
 */
// TODO DA: layout placeholder
@Composable
fun CyclePhaseSection(
    modifier: Modifier = Modifier.fillMaxWidth(),
    entries: List<CyclePhaseEntry> = emptyList(),
    onClick: () -> Unit = {},
) {
    CardWithHeader(
        modifier = modifier,
        headerInfos = HeaderInfos(
            endIconRes = CoreRes.drawable.ic_chevron_end,
            labelRes = CoreRes.string.cycle_phase,
            onClick = onClick,
        ),
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(entries) { entry ->
                InternalCard(
                    modifier = Modifier.width(120.dp).height(96.dp),
                    title = entry.dateLabel,
                    subTitleRes = entry.phaseLabel,
                    iconRes = entry.iconRes
                )
            }
        }
    }
}
