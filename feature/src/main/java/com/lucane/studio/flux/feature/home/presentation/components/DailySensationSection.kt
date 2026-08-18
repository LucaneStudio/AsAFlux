package com.lucane.studio.flux.feature.home.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lucane.studio.flux.core.ui.cards.CardWithHeader
import com.lucane.studio.flux.core.ui.model.HeaderInfos
import com.lucane.studio.flux.core.R as CoreRes

/** Entry point to the (not yet built) daily-sensation / symptom logging flow. */
// TODO DA: layout placeholder — reuses the shared CardWithHeader, no destination wired yet
@Composable
fun DailySensationSection(
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit = {},
) {
    CardWithHeader(
        modifier = modifier,
        headerInfos = HeaderInfos(
            endIconRes = CoreRes.drawable.ic_chevron_end,
            labelRes = CoreRes.string.daily_sensation,
            onClick = onClick,
        ),
    ) { }
}
