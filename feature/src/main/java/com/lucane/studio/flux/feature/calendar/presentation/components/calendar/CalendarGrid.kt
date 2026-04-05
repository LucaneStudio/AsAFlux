package com.lucane.studio.flux.feature.calendar.presentation.components.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state.CalendarDayUiState

/**
 * Renders the 7-column calendar grid.
 *
 * Expects exactly 35 or 42 cells (5 or 6 full weeks) from the ViewModel.
 * Leading/trailing cells outside the current month have [com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state.CalendarDayUiState.isCurrentMonth] = false.
 */
@Composable
fun CalendarGrid(
    modifier: Modifier = Modifier,
    days: List<CalendarDayUiState>,
) {
    val weeks = days.chunked(7)

    Column(
        modifier = modifier,
    ) {
        CalendarWeekHeader()

        weeks.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                week.forEach { day ->
                    CalendarDayCell(
                        state = day,
                    )
                }
            }
        }
    }
}