package com.lucane.studio.flux.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.ui.cards.CardWithHeader
import com.lucane.studio.flux.core.ui.cards.InternalCard
import com.lucane.studio.flux.core.ui.cards.InternalCardStyle
import com.lucane.studio.flux.core.ui.model.HeaderInfos
import com.lucane.studio.flux.core.R as CoreRes

/**
 * "My cycles" section — header + subtitle + two stat tiles, reusing the
 * shared [CardWithHeader]/[CycleStatCard].
 *
 * [averageBleedingDurationDays] / [averageCycleLengthDays] already exist as
 * real data in [com.lucane.studio.flux.data.local.datastore.SettingsDataStore]
 * and could back the tiles directly; [cyclesEnteredCount] has no backing query
 * yet — all three are passed as example values for now pending a decision on
 * wiring this section up.
 */
// TODO DA: layout placeholder
@Composable
fun MyCyclesSection(
    modifier: Modifier = Modifier.fillMaxWidth(),
    cyclesEnteredCount: Int,
    averageBleedingDurationDays: Int,
    averageCycleLengthDays: Int,
    onClick: () -> Unit = {},
) {
    CardWithHeader(
        modifier = modifier,
        headerInfos = HeaderInfos(
            endIconRes = CoreRes.drawable.ic_chevron_end,
            labelRes = CoreRes.string.my_cycles,
            subLabel = stringResource(CoreRes.string.cycles_entered, cyclesEnteredCount),
            onClick = onClick,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            InternalCard(
                modifier = Modifier.weight(1f).height(96.dp),
                title = "$averageBleedingDurationDays " + stringResource(CoreRes.string.common_days),
                subTitleRes = CoreRes.string.average_bleeding_duration,
                iconRes = CoreRes.drawable.ic_period,
                style = InternalCardStyle.BOTTOM
            )

            InternalCard(
                modifier = Modifier.weight(1f).height(96.dp),
                title = "$averageCycleLengthDays " + stringResource(CoreRes.string.common_days),
                subTitleRes = CoreRes.string.average_cycle_length,
                iconRes = CoreRes.drawable.ic_cycle,
                style = InternalCardStyle.BOTTOM
            )
        }
    }
}
