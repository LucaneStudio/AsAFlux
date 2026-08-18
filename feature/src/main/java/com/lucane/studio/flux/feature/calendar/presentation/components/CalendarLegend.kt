package com.lucane.studio.flux.feature.calendar.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.R as CoreRes

/** Small legend row explaining the calendar's day-cell markers. */
// TODO DA: layout placeholder — pending the final visual design pass
@Composable
fun CalendarLegend(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        LegendItem(iconRes = CoreRes.drawable.ic_legend_period, labelRes = CoreRes.string.tracking_legend_bleeding)
        LegendItem(iconRes = CoreRes.drawable.ic_legend_fertility, labelRes = CoreRes.string.tracking_legend_fertility)
        LegendItem(iconRes = CoreRes.drawable.ic_legend_ovum, labelRes = CoreRes.string.tracking_legend_ovulation)
        LegendItem(iconRes = CoreRes.drawable.ic_legend_symptoms, labelRes = CoreRes.string.tracking_legend_symptoms)
    }
}

@Composable
private fun LegendItem(
    @DrawableRes iconRes: Int,
    @StringRes labelRes: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Image(
            modifier = Modifier.size(10.dp),
            painter = painterResource(iconRes),
            contentDescription = null
        )
        Text(
            text = stringResource(labelRes),
            style = TextStyle(
                fontFamily = AsAFont.regular,
                fontSize = 12.sp,
                color = AsAColors.purpleGray
            ),
        )
    }
}
