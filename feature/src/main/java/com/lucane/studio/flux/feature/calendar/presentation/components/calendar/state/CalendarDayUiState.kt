package com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state

/**
 * UI state for a single calendar cell.
 *
 * The ViewModel builds a flat list of 42 cells (6 weeks × 7 days)
 * covering the full grid, including leading/trailing days from
 * adjacent months.
 *
 * @param dayOfMonth  Day number to display (1–31).
 * @param isCurrentMonth  False for leading/trailing padding days.
 * @param isSelected  True for the currently selected/today day.
 * @param isPeriod    True if a period flow was logged for this day.
 * @param hasLog      True if any daily log exists (shows dot indicator).
 */
data class CalendarDayUiState(
    val dayOfMonth: Int,
    val isCurrentMonth: Boolean,
    val isSelected: Boolean,
    val isPeriod: Boolean,
    val hasLog: Boolean,
    val isOvulation: Boolean,
    val isFertile: Boolean,
    val isNextPeriod: Boolean,
)