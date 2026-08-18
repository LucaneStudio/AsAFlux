package com.lucane.studio.flux.feature.calendar.presentation

import java.time.LocalDate

/**
 * UI state for a single calendar cell.
 *
 * The ViewModel builds a flat list of 35 or 42 cells (5 or 6 weeks × 7 days)
 * covering the full grid, including leading/trailing days from adjacent months.
 *
 * @param date            The actual date this cell represents — leading/trailing
 *                        cells belong to the adjacent month, so this (not
 *                        [dayOfMonth] + the displayed month) is what click
 *                        handlers must use to select a day unambiguously.
 * @param dayOfMonth      Day number to display (1–31).
 * @param isCurrentMonth  False for leading/trailing padding days.
 * @param isSelected      True for the currently selected day.
 * @param isPeriod        True if a period flow was logged for this day.
 * @param isPeriodStart   True if this is the first day of a consecutive flow streak.
 *                        Used to render the left-rounded cap of the period range pill.
 * @param isPeriodEnd     True if this is the last day of a consecutive flow streak.
 *                        Used to render the right-rounded cap of the period range pill.
 * @param hasLog          True if any daily log exists for this day.
 * @param hasDeclaration  True if this day has symptoms, a pain level, a note,
 *                        or a flow intensity declared outside of a period
 *                        (e.g. spotting) — shows the dot indicator. A plain
 *                        period day with no extra declaration doesn't get one,
 *                        since the pill fill already marks it.
 * @param isOvulation      True if this day is the predicted ovulation date.
 * @param isFertile        True if this day falls within the predicted fertile window.
 * @param isFertileStart   True if this is the first day of the predicted fertile window.
 *                         Used to render the left-rounded cap of its range pill.
 * @param isFertileEnd     True if this is the last day of the predicted fertile window.
 *                         Used to render the right-rounded cap of its range pill.
 * @param isNextPeriod     True if this day falls within the predicted next period range.
 * @param isNextPeriodStart True if this is the first day of the predicted next period range.
 *                         Used to render the left-rounded cap of its range pill.
 * @param isNextPeriodEnd  True if this is the last day of the predicted next period range.
 *                         Used to render the right-rounded cap of its range pill.
 *
 * ## Start / End combinations (applies to period, fertile window, and next period alike)
 * | ...Start | ...End | Rendered shape              |
 * |----------|--------|------------------------------|
 * | true     | true   | Full pill (single-day)      |
 * | true     | false  | Left-rounded, right-flat    |
 * | false    | true   | Left-flat, right-rounded    |
 * | false    | false  | Flat rectangle (middle day) |
 */
data class CalendarDayUiState(
    val date: LocalDate,
    val dayOfMonth: Int,
    val isCurrentMonth: Boolean,
    val isSelected: Boolean,
    val isPeriod: Boolean,
    val isPeriodStart: Boolean,
    val isPeriodEnd: Boolean,
    val hasLog: Boolean,
    val hasDeclaration: Boolean,
    val isOvulation: Boolean,
    val isFertile: Boolean,
    val isFertileStart: Boolean,
    val isFertileEnd: Boolean,
    val isNextPeriod: Boolean,
    val isNextPeriodStart: Boolean,
    val isNextPeriodEnd: Boolean,
)