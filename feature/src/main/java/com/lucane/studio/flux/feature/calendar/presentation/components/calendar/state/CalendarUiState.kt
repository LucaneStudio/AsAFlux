package com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state

import com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state.CalendarDayUiState
import java.time.LocalDate
import java.time.YearMonth

/**
 * Full UI state for the calendar screen.
 *
 * Sealed to represent the three possible states:
 * loading, error, and data ready.
 */
sealed interface CalendarUiState {

    data object Loading : CalendarUiState

    data class Error(val message: String) : CalendarUiState

    data class Success(
        val currentMonth: YearMonth,
        val monthNumber: String,          // e.g. "01"
        val monthName: String,            // e.g. "janvier" (system locale)
        val days: List<CalendarDayUiState>,
        val selectedDate: LocalDate,
        val daysRemaining: Int?,          // null = not enough data to predict
        val isPeriodActive: Boolean,      // true if selected day has flow != NONE
        val nextPeriodDate: LocalDate?,
        val ovulationDate: LocalDate?,
        val fertileWindowStart: LocalDate?,
        val fertileWindowEnd: LocalDate?,
        val nextPeriodEnd: LocalDate?,
    ) : CalendarUiState
}