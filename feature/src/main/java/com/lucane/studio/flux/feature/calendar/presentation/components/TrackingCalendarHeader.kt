package com.lucane.studio.flux.feature.calendar.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.lucane.studio.flux.core.ui.inputs.buttons.IconLightButton
import com.lucane.studio.flux.core.R as CoreRes

/** Month navigation header for the tracking screen: "‹ 01 janvier ›". */
// TODO DA: layout placeholder — pending the final visual design pass
@Composable
fun TrackingCalendarHeader(
    modifier: Modifier = Modifier,
    monthNumber: String,
    monthName: String,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Reuses the ">" chevron asset, mirrored — no dedicated "previous" icon exists yet.
        IconLightButton(
            modifier = Modifier.graphicsLayer(rotationZ = 180f),
            icon = painterResource(CoreRes.drawable.ic_chevron_end),
            onClick = onPreviousMonth,
        )
        CalendarMonthLabel(monthNumber = monthNumber, monthName = monthName)
        IconLightButton(
            icon = painterResource(CoreRes.drawable.ic_chevron_end),
            onClick = onNextMonth,
        )
    }
}
