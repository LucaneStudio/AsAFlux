package com.lucane.studio.flux.feature.calendar.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.feature.calendar.presentation.CalendarDayUiState
import java.time.LocalDate
import com.lucane.studio.flux.core.R as CoreRes

/**
 * 7-column calendar grid for the tracking screen, using [TrackingDayCell]
 * instead of Home's [CalendarDayCell] — same [CalendarDayUiState] data
 * (same [com.lucane.studio.flux.feature.calendar.presentation.CalendarViewModel]),
 * different rendering to match the provided mockup. Full-width (weighted
 * columns), unlike Home's fixed-size compact widget.
 */
// TODO DA: layout placeholder — pending the final visual design pass
@Composable
fun TrackingCalendarGrid(
    modifier: Modifier = Modifier,
    days: List<CalendarDayUiState>,
    onDayClick: (LocalDate) -> Unit = {},
) {
    val weeks = days.chunked(7)
    val dayLabels = stringArrayResource(CoreRes.array.calendar_week_days)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            dayLabels.forEach { label ->
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = label,
                        style = TextStyle(fontFamily = AsAFont.regular, fontSize = 14.sp, color = AsAColors.black),
                    )
                }
            }
        }

        weeks.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    TrackingDayCell(
                        modifier = Modifier.weight(1f),
                        state = day,
                        onClick = { onDayClick(day.date) },
                    )
                }
            }
        }
    }
}
