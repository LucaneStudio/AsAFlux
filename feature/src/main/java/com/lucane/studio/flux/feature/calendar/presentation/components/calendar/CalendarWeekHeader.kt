package com.lucane.studio.flux.feature.calendar.presentation.components.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.feature.R
import com.lucane.studio.flux.core.R as CoreRes


@Composable
fun CalendarWeekHeader(
    modifier: Modifier = Modifier,
) {
    // Ordered Mon → Sun, resolved from strings (localisable)
    val dayLabels = stringArrayResource(CoreRes.array.calendar_week_days)

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        dayLabels.forEach { label ->
            Box(Modifier.size(29.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = label,
                    style = TextStyle(
                        fontFamily = AsAFont.regular,
                        fontSize = 14.sp,
                        color = AsAColors.black,
                    ),
                )
            }
        }
    }
}