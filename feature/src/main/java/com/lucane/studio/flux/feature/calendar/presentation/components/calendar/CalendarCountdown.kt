package com.lucane.studio.flux.feature.calendar.presentation.components.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.R as CoreRes

/**
 * Right-side countdown panel: big number + label below.
 *
 * @param daysRemaining  Days until next predicted period. Null when unknown (shows "—").
 */
@Composable
fun CalendarCountdown(
    modifier: Modifier = Modifier,
    daysRemaining: Int?,
) {
    Column(
        modifier = modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = daysRemaining?.toString() ?: "—",
            style = TextStyle(
                fontFamily = AsAFont.medium,
                fontWeight = FontWeight.Medium,
                fontSize = 68.sp,
                color = AsAColors.black,
                lineHeight = 68.sp,
            ),
        )
        Text(
            text = stringResource(CoreRes.string.calendar_countdown_label),
            style = TextStyle(
                fontFamily = AsAFont.regular,
                fontSize = 12.sp,
                color = AsAColors.purpleGray,
                textAlign = TextAlign.Center,
            ),
        )
    }
}