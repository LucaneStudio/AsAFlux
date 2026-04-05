package com.lucane.studio.flux.feature.calendar.presentation.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucane.studio.flux.data.local.datastore.UserPreferencesDataStore
import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.feature.calendar.domain.usecase.GetCycleHistoryUseCase
import com.lucane.studio.flux.feature.calendar.domain.usecase.GetMonthLogsUseCase
import com.lucane.studio.flux.feature.calendar.domain.usecase.TogglePeriodUseCase
import com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state.CalendarDayUiState
import com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state.CalendarUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
    private val togglePeriod: TogglePeriodUseCase,
    private val prefsDataStore: UserPreferencesDataStore,
) : ViewModel() {

    // ── Internal state ────────────────────────────────────────────────────────

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    // ── Public UI state ───────────────────────────────────────────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CalendarUiState> = _currentMonth
        .flatMapLatest { month ->
            combine(
                getMonthLogs(month),  // logs du mois affiché → grille
                getCycleHistory(),    // 6 mois d'historique → calcul des cycles
                _selectedDate,
                prefsDataStore.userPreferences.map { it.showOvulation },
                prefsDataStore.userPreferences.map { it.showFertileWindow }
            ) { monthLogs, history, selectedDate, showOvulation, showFertility ->
                buildUiState(month, monthLogs, history, selectedDate, showOvulation, showFertility)
            }
        }
        .catch { e -> emit(CalendarUiState.Error(e.message ?: "Unknown error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CalendarUiState.Loading,
        )

    // ── Display toggles ───────────────────────────────────────────────────────

    private val _showOvulation     = prefsDataStore.userPreferences
        .map { it.showOvulation }
    private val _showFertility = prefsDataStore.userPreferences
        .map { it.showFertileWindow }

    val showOvulation: StateFlow<Boolean> = _showOvulation
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val showFertility: StateFlow<Boolean> = _showFertility
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun onToggleOvulation() {
        viewModelScope.launch {
            prefsDataStore.updateShowOvulation(!showOvulation.value)
        }
    }

    fun onToggleFertileWindow() {
        viewModelScope.launch {
            prefsDataStore.updateShowFertileWindow(!showFertility.value)
        }
    }

    // ── Event handlers ────────────────────────────────────────────────────────

    fun onDayClick(dayOfMonth: Int) {
        _selectedDate.value = _currentMonth.value.atDay(dayOfMonth)
    }

    fun onPreviousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    fun onNextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    fun onPeriodToggle() {
        viewModelScope.launch {
            togglePeriod(_selectedDate.value)
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private fun buildUiState(
        month: YearMonth,
        monthLogs: List<DailyLog>,
        history: List<DailyLog>,
        selectedDate: LocalDate,
        showOvulation: Boolean,
        showFertileWindow: Boolean,
    ): CalendarUiState {
        val logsByDate      = monthLogs.associateBy { it.date }
        val nextPeriodDate  = computeNextPeriodDate(history)
        val ovulationDate   = nextPeriodDate?.minusDays(14)
        val fertileStart    = ovulationDate?.minusDays(5)
        val fertileEnd      = ovulationDate?.plusDays(1)
        val avgPeriodLength = computeAvgPeriodLength(history)
        val nextPeriodEnd   = nextPeriodDate?.plusDays(avgPeriodLength - 1)
        val daysRemaining   = nextPeriodDate?.let {
            ChronoUnit.DAYS.between(LocalDate.now(), it).toInt().takeIf { d -> d >= 0 }
        }

        // Apply toggles before passing to cells
        val displayedOvulation    = if (showOvulation) ovulationDate else null
        val displayedFertileStart = if (showFertileWindow) fertileStart else null
        val displayedFertileEnd   = if (showFertileWindow) fertileEnd else null

        return CalendarUiState.Success(
            currentMonth       = month,
            monthNumber        = month.format(DateTimeFormatter.ofPattern("MM")),
            monthName          = month.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            days               = buildDayCells(
                month          = month,
                logsByDate     = logsByDate,
                selectedDate   = selectedDate,
                ovulationDate  = displayedOvulation,     // ← filtré
                fertileStart   = displayedFertileStart,  // ← filtré
                fertileEnd     = displayedFertileEnd,    // ← filtré
                nextPeriodDate = nextPeriodDate,
                nextPeriodEnd  = nextPeriodEnd,
            ),
            selectedDate       = selectedDate,
            daysRemaining      = daysRemaining,
            isPeriodActive     = logsByDate[selectedDate]?.flowIntensity
                .let { it != null && it != FlowIntensity.NONE },
            nextPeriodDate     = nextPeriodDate,
            nextPeriodEnd      = nextPeriodEnd,
            ovulationDate      = displayedOvulation,
            fertileWindowStart = displayedFertileStart,
            fertileWindowEnd   = displayedFertileEnd,
        )
    }

    /**
     * Builds a flat list of 35 or 42 [CalendarDayUiState] cells covering
     * the full grid (Mon → Sun, 5 or 6 weeks).
     *
     * Leading cells from the previous month and trailing cells from the
     * next month are included with [CalendarDayUiState.isCurrentMonth] = false.
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
            val date = firstOfMonth.minusDays(leadingDays.toLong()).plusDays(index.toLong())
            val log  = logsByDate[date]

            CalendarDayUiState(
                dayOfMonth     = date.dayOfMonth,
                isCurrentMonth = date.month == month.month,
                isSelected     = date == selectedDate,
                isPeriod       = log?.flowIntensity
                    .let { it != null && it != FlowIntensity.NONE },
                hasLog         = log != null,
                isOvulation    = date == ovulationDate,
                isFertile      = fertileStart != null && fertileEnd != null
                        && !date.isBefore(fertileStart) && !date.isAfter(fertileEnd),
                isNextPeriod   = nextPeriodDate != null && nextPeriodEnd != null   // ← nouveau
                        && !date.isBefore(nextPeriodDate) && !date.isAfter(nextPeriodEnd),
            )
        }
    }

    /**
     * Returns the predicted date of the next period, or null if not enough data.
     *
     * Collects period-start dates (first day of each consecutive flow streak),
     * then computes the average cycle length from the last 3 starts.
     *
     * Uses [ChronoUnit.DAYS] instead of [java.time.Period.days] to correctly
     * count days across month boundaries.
     */
    private fun computeNextPeriodDate(logs: List<DailyLog>): LocalDate? {
        android.util.Log.d("CalendarVM", "history size: ${logs.size}")

        val sortedDates = logs
            .filter { it.flowIntensity != FlowIntensity.NONE }
            .map { it.date }
            .sorted()

        // Track both the streak starts AND the last date seen
        data class Acc(val starts: List<LocalDate>, val lastSeen: LocalDate?)

        val result = sortedDates.fold(Acc(emptyList(), null)) { acc, date ->
            if (acc.lastSeen == null || ChronoUnit.DAYS.between(acc.lastSeen, date) > 1) {
                Acc(acc.starts + date, date)   // new streak
            } else {
                Acc(acc.starts, date)          // continuation — update lastSeen only
            }
        }

        val periodStarts = result.starts
        android.util.Log.d("CalendarVM", "periodStarts: $periodStarts")

        if (periodStarts.size < 2) {
            android.util.Log.d("CalendarVM", "not enough starts → null")
            return null
        }

        val recentStarts   = periodStarts.takeLast(3)
        val avgCycleLength = recentStarts
            .zipWithNext { a, b -> ChronoUnit.DAYS.between(a, b) }
            .average()
            .toLong()

        android.util.Log.d("CalendarVM", "periodStarts: $periodStarts")
        android.util.Log.d("CalendarVM", "recentStarts: $recentStarts")
        android.util.Log.d("CalendarVM", "avgCycle: $avgCycleLength")
        android.util.Log.d("CalendarVM", "nextPeriod: ${recentStarts.last().plusDays(avgCycleLength)}")
        android.util.Log.d("CalendarVM", "today: ${LocalDate.now()}")
        android.util.Log.d("CalendarVM", "daysRemaining: ${ChronoUnit.DAYS.between(LocalDate.now(), recentStarts.last().plusDays(avgCycleLength))}")

        return recentStarts.last().plusDays(avgCycleLength)
    }

    private fun computeAvgPeriodLength(logs: List<DailyLog>): Long {
        // Group consecutive flow days into streaks, measure each streak length
        val sortedDates = logs
            .filter { it.flowIntensity != FlowIntensity.NONE }
            .map { it.date }
            .sorted()

        if (sortedDates.isEmpty()) return 5L  // default fallback

        data class Acc(
            val streaks: List<Int>,
            val currentStreak: Int,
            val lastSeen: LocalDate?,
        )

        val result = sortedDates.fold(Acc(emptyList(), 1, null)) { acc, date ->
            when {
                acc.lastSeen == null -> Acc(acc.streaks, 1, date)
                ChronoUnit.DAYS.between(acc.lastSeen, date) == 1L ->
                    Acc(acc.streaks, acc.currentStreak + 1, date)  // continuation
                else ->
                    Acc(acc.streaks + acc.currentStreak, 1, date)  // new streak
            }
        }

        val allStreaks = result.streaks + result.currentStreak
        return allStreaks.average().toLong().coerceAtLeast(1L)
    }
}