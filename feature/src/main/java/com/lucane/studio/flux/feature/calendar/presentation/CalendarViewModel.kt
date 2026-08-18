package com.lucane.studio.flux.feature.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.model.Symptom
import com.lucane.studio.flux.data.model.SymptomCategory
import com.lucane.studio.flux.data.repository.SymptomRepository
import com.lucane.studio.flux.feature.calendar.domain.usecase.EndPeriodUseCase
import com.lucane.studio.flux.feature.calendar.domain.usecase.GetCycleHistoryUseCase
import com.lucane.studio.flux.feature.calendar.domain.usecase.GetCyclePredictionsUseCase
import com.lucane.studio.flux.feature.calendar.domain.usecase.GetMonthLogsUseCase
import com.lucane.studio.flux.feature.calendar.domain.usecase.LogDayUseCase
import com.lucane.studio.flux.feature.calendar.domain.usecase.StartPeriodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getMonthLogs: GetMonthLogsUseCase,
    private val getCycleHistory: GetCycleHistoryUseCase,
    private val getCyclePredictions: GetCyclePredictionsUseCase,
    private val startPeriod: StartPeriodUseCase,
    private val endPeriod: EndPeriodUseCase,
    private val logDay: LogDayUseCase,
    private val settingsDataStore: SettingsDataStore,
    private val symptomRepository: SymptomRepository,
) : ViewModel() {

    // ── Internal state ────────────────────────────────────────────────────────

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    /** Cycle averages + symptom catalog grouped so the main [combine] stays typed (≤ 5 sources). */
    private val settingsAndSymptoms: Flow<SettingsAndSymptoms> = combine(
        settingsDataStore.averageCycleLength,
        settingsDataStore.averageBleedingDuration,
        symptomRepository.getAllSymptoms(),
    ) { cycleLength, bleedingDuration, allSymptoms ->
        SettingsAndSymptoms(cycleLength, bleedingDuration, allSymptoms)
    }

    // ── Public UI state ───────────────────────────────────────────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CalendarUiState> = _currentMonth
        .flatMapLatest { month ->
            combine(
                getMonthLogs(month),
                getCycleHistory(),
                _selectedDate,
                settingsAndSymptoms,
            ) { monthLogs, history, selectedDate, settings ->
                buildUiState(
                    month               = month,
                    monthLogs           = monthLogs,
                    history             = history,
                    selectedDate        = selectedDate,
                    avgCycleLength      = settings.cycleLength,
                    avgBleedingDuration = settings.bleedingDuration,
                    allSymptoms         = settings.allSymptoms,
                )
            }
        }
        .catch { e -> emit(CalendarUiState.Error(e.message ?: "Unknown error")) }
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = CalendarUiState.Loading,
        )

    // ── Event handlers ────────────────────────────────────────────────────────

    /** Selects a day directly by date — safe for leading/trailing cells from adjacent months. */
    fun onDaySelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onPreviousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    fun onNextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    /**
     * Called when the user taps the "start period" CTA.
     * Records the selected date as the first day of bleeding.
     */
    fun onPeriodStart() {
        viewModelScope.launch {
            startPeriod(_selectedDate.value)
        }
    }

    /**
     * Called when the user taps the "end period" CTA.
     * Records the selected date as the last day of bleeding,
     * cleans up any excess flow days, and triggers a stat recalculation.
     */
    fun onPeriodEnd() {
        viewModelScope.launch {
            endPeriod(_selectedDate.value)
        }
    }

    /**
     * Called when the user taps "valider" on the tracking screen's symptom
     * declaration panel — saves the full day for [_selectedDate].
     */
    fun onSaveDayLog(
        flowIntensity: FlowIntensity,
        painLevel: Int?,
        symptoms: List<Symptom>,
        note: String?,
    ) {
        viewModelScope.launch {
            logDay(
                date          = _selectedDate.value,
                flowIntensity = flowIntensity,
                painLevel     = painLevel,
                symptoms      = symptoms,
                notes         = note,
            )
        }
    }

    /**
     * Adds a user-defined symptom to the catalog (persisted alongside the
     * presets, [Symptom.isCustom] = true) and hands it back via [onAdded] —
     * with its generated id — so the caller can select it immediately in
     * the day's staged declaration without waiting for [uiState] to refresh.
     */
    fun onAddCustomSymptom(name: String, category: SymptomCategory, onAdded: (Symptom) -> Unit) {
        viewModelScope.launch {
            val trimmedName = name.trim()
            if (trimmedName.isEmpty()) return@launch

            val id = symptomRepository.upsertSymptom(
                Symptom(name = trimmedName, category = category, isCustom = true)
            )
            onAdded(Symptom(id = id, name = trimmedName, category = category, isCustom = true))
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private fun buildUiState(
        month: YearMonth,
        monthLogs: List<DailyLog>,
        history: List<DailyLog>,
        selectedDate: LocalDate,
        avgCycleLength: Int,
        avgBleedingDuration: Int,
        allSymptoms: List<Symptom>,
    ): CalendarUiState {
        val logsByDate      = monthLogs.associateBy { it.date }
        // Period-active state and the current-period countdown must reflect *today*,
        // never the month the user happens to be browsing. They are computed from
        // [history] (which always contains today and the ongoing streak), not from
        // [monthLogs] which only covers the displayed month.
        val historyByDate   = history.associateBy { it.date }
        val isPeriodActive  = computeIsPeriodActive(historyByDate)
        val predictions     = getCyclePredictions(history, avgCycleLength, avgBleedingDuration)
        val nextPeriodDate  = predictions.nextPeriodStart
        val ovulationDate   = predictions.ovulationDate
        val fertileStart    = predictions.fertileWindowStart
        val fertileEnd      = predictions.fertileWindowEnd
        val nextPeriodEnd   = predictions.nextPeriodEnd

        /**
         * Countdown logic:
         * - Period active   → days remaining in the current period
         *                     = (periodStart + avgBleedingDuration - 1) - today
         * - Period inactive → days until next predicted period start
         */
        val daysRemaining = if (isPeriodActive) {
            computeCurrentPeriodDaysRemaining(historyByDate, avgBleedingDuration)
        } else {
            nextPeriodDate?.let {
                ChronoUnit.DAYS.between(LocalDate.now(), it).toInt().takeIf { d -> d >= 0 }
            }
        }

        // [logsByDate] (from [monthLogs]) covers the whole displayed month
        // regardless of today's date, so it's the reliable source for a day
        // the user can currently see and select — including days after
        // tomorrow, which [history] deliberately excludes (it's a backward-
        // looking window for cycle stats). [historyByDate] is only a fallback
        // for the edge case where the selected day is outside the displayed
        // month (e.g. selected, then navigated away).
        val selectedDayLog = logsByDate[selectedDate] ?: historyByDate[selectedDate]

        return CalendarUiState.Success(
            currentMonth       = month,
            monthNumber        = month.format(DateTimeFormatter.ofPattern("MM")),
            monthName          = month.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            days               = buildDayCells(
                month          = month,
                logsByDate     = logsByDate,
                selectedDate   = selectedDate,
                ovulationDate  = ovulationDate,
                fertileStart   = fertileStart,
                fertileEnd     = fertileEnd,
                nextPeriodDate = nextPeriodDate,
                nextPeriodEnd  = nextPeriodEnd,
            ),
            selectedDate       = selectedDate,
            daysRemaining      = daysRemaining,
            isPeriodActive     = isPeriodActive,
            nextPeriodDate     = nextPeriodDate,
            nextPeriodEnd      = nextPeriodEnd,
            ovulationDate      = ovulationDate,
            fertileWindowStart = fertileStart,
            fertileWindowEnd   = fertileEnd,
            selectedDayFlowIntensity = selectedDayLog?.flowIntensity ?: FlowIntensity.NONE,
            selectedDayPainLevel     = selectedDayLog?.painLevel,
            selectedDaySymptoms      = selectedDayLog?.symptoms ?: emptyList(),
            selectedDayNote          = selectedDayLog?.notes,
            allSymptoms              = allSymptoms,
        )
    }

    /**
     * Determines whether a period is currently active (ongoing today).
     * Independent of [_selectedDate]: the CTA reflects the real-world state.
     */
    private fun computeIsPeriodActive(logsByDate: Map<LocalDate, DailyLog>): Boolean {
        val todayLog = logsByDate[LocalDate.now()] ?: return false
        return todayLog.isPeriod
    }

    /**
     * Returns the number of days remaining in the current active period.
     *
     * Walks backwards from today to find the period start date (first day of
     * the current consecutive streak), then computes:
     * expectedEnd = periodStart + avgBleedingDuration - 1
     * remaining   = expectedEnd - today  (minimum 0 — never negative)
     *
     * Returns null if no period start can be found (should not happen when
     * called while [computeIsPeriodActive] returns true, but guarded for safety).
     */
    private fun computeCurrentPeriodDaysRemaining(
        logsByDate: Map<LocalDate, DailyLog>,
        avgBleedingDuration: Int,
    ): Int? {
        val today = LocalDate.now()

        // Walk back to find the first day of the current streak
        var periodStart = today
        while (true) {
            val previous    = periodStart.minusDays(1)
            val previousLog = logsByDate[previous]
            if (previousLog == null || !previousLog.isPeriod) break
            periodStart = previous
        }

        val expectedEnd   = periodStart.plusDays(avgBleedingDuration.toLong() - 1)
        val daysRemaining = ChronoUnit.DAYS.between(today, expectedEnd).toInt()

        return daysRemaining.coerceAtLeast(0)
    }

    /**
     * Builds a flat list of 35 or 42 [CalendarDayUiState] cells covering
     * the full grid (Mon → Sun, 5 or 6 weeks).
     *
     * Leading cells from the previous month and trailing cells from the
     * next month are included with [CalendarDayUiState.isCurrentMonth] = false.
     *
     * [isPeriodStart] and [isPeriodEnd] are computed by comparing each cell
     * to its immediate neighbours in [logsByDate], enabling the pill/range visual.
     */
    private fun buildDayCells(
        month: YearMonth,
        logsByDate: Map<LocalDate, DailyLog>,
        selectedDate: LocalDate,
        ovulationDate: LocalDate?,
        fertileStart: LocalDate?,
        fertileEnd: LocalDate?,
        nextPeriodDate: LocalDate?,
        nextPeriodEnd: LocalDate?,
    ): List<CalendarDayUiState> {
        val firstOfMonth = month.atDay(1)
        val leadingDays  = (firstOfMonth.dayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7
        val totalCells   = leadingDays + month.lengthOfMonth()
        val gridSize     = if (totalCells <= 35) 35 else 42

        return (0 until gridSize).map { index ->
            val date      = firstOfMonth.minusDays(leadingDays.toLong()).plusDays(index.toLong())
            val log       = logsByDate[date]
            val prevLog   = logsByDate[date.minusDays(1)]
            val nextLog   = logsByDate[date.plusDays(1)]

            val hasPeriod     = log?.isPeriod == true
            val prevHasPeriod = prevLog?.isPeriod == true
            val nextHasPeriod = nextLog?.isPeriod == true

            // An explicitly declared flow intensity outside of a period (e.g.
            // spotting) doesn't get the pill fill, so it needs the dot to stay
            // visible on the calendar — same as any other declared symptom.
            val hasIndependentFlow = log != null && !hasPeriod
                    && log.flowIntensity != FlowIntensity.NONE
                    && log.flowIntensity != FlowIntensity.NOT_DECLARED

            CalendarDayUiState(
                date           = date,
                dayOfMonth     = date.dayOfMonth,
                isCurrentMonth = date.month == month.month,
                isSelected     = date == selectedDate,
                isPeriod       = hasPeriod,
                isPeriodStart  = hasPeriod && !prevHasPeriod,
                isPeriodEnd    = hasPeriod && !nextHasPeriod,
                hasLog         = log != null,
                hasDeclaration = log != null &&
                        (log.symptoms.isNotEmpty() || log.painLevel != null || !log.notes.isNullOrBlank() || hasIndependentFlow),
                isOvulation    = date == ovulationDate,
                isFertile      = fertileStart != null && fertileEnd != null
                        && !date.isBefore(fertileStart) && !date.isAfter(fertileEnd),
                isFertileStart = fertileStart != null && date == fertileStart,
                isFertileEnd   = fertileEnd != null && date == fertileEnd,
                isNextPeriod   = nextPeriodDate != null && nextPeriodEnd != null
                        && !date.isBefore(nextPeriodDate) && !date.isAfter(nextPeriodEnd),
                isNextPeriodStart = nextPeriodDate != null && date == nextPeriodDate,
                isNextPeriodEnd   = nextPeriodEnd != null && date == nextPeriodEnd,
            )
        }
    }

    // ── Internal models ─────────────────────────────────────────────────────────

    private data class SettingsAndSymptoms(
        val cycleLength: Int,
        val bleedingDuration: Int,
        val allSymptoms: List<Symptom>,
    )
}
