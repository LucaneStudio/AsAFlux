package com.lucane.studio.flux.feature.calendar.domain.usecase

import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import com.lucane.studio.flux.data.repository.DailyLogRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Recalculates cycle statistics after a period has been declared complete.
 *
 * ## Counting rule
 * Stats are only updated once **6 or more complete cycles** have been recorded.
 * Until then, the values set during onboarding are kept unchanged so predictions
 * remain meaningful from day one.
 *
 * ## Definition of a "cycle"
 * A cycle is detected as a streak of consecutive days with flowIntensity != NONE.
 * Each distinct streak = one recorded cycle.
 *
 * ## What is updated
 * - [SettingsDataStore.averageCycleLength] : rolling average of the last 6 cycle
 *   lengths (days between consecutive streak start dates), clamped to [15, 60].
 * - [SettingsDataStore.averageBleedingDuration] : rolling average of the last 6
 *   completed streak durations (only past streaks are counted, the ongoing one is
 *   excluded), clamped to [1, 15].
 */
class UpdateCycleStatsUseCase @Inject constructor(
    private val repository: DailyLogRepository,
    private val settingsDataStore: SettingsDataStore,
    private val detectCycleStreaks: DetectCycleStreaksUseCase,
) {
    suspend operator fun invoke() {
        val allPeriodLogs = repository.getAllPeriodLogs()
        val streaks       = detectCycleStreaks(allPeriodLogs)

        // Need at least 6 recorded cycles before overriding onboarding values.
        if (streaks.size < MIN_CYCLES) return

        updateCycleLength(streaks)
        updateBleedingDuration(streaks)
    }

    // ─── Stat calculations ────────────────────────────────────────────────────

    /**
     * Average cycle length = average of the intervals between consecutive streak
     * start dates, computed on the last [ROLLING_WINDOW] cycles.
     */
    private suspend fun updateCycleLength(streaks: List<CycleStreak>) {
        val recentStarts = streaks.takeLast(ROLLING_WINDOW + 1).map { it.start }
        if (recentStarts.size < 2) return

        val intervals = recentStarts
            .zipWithNext { a, b -> ChronoUnit.DAYS.between(a, b).toInt() }

        val average = intervals.average().roundToInt().coerceIn(MIN_CYCLE_LENGTH, MAX_CYCLE_LENGTH)
        settingsDataStore.setAverageCycleLength(average)
    }

    /**
     * Average bleeding duration = average of completed streak durations.
     * The last streak is excluded if it ends today or later (still potentially
     * ongoing), using the last [ROLLING_WINDOW] completed ones.
     */
    private suspend fun updateBleedingDuration(streaks: List<CycleStreak>) {
        val today = LocalDate.now()

        val completedStreaks = streaks
            .filter { it.end < today }   // exclude any streak still potentially ongoing
            .takeLast(ROLLING_WINDOW)

        if (completedStreaks.isEmpty()) return

        val average = completedStreaks
            .map { it.durationDays }
            .average()
            .roundToInt()
            .coerceIn(MIN_BLEEDING_DURATION, MAX_BLEEDING_DURATION)

        settingsDataStore.setAverageBleedingDuration(average)
    }

    // ─── Constants ────────────────────────────────────────────────────────────

    private companion object {
        const val MIN_CYCLES           = 6
        const val ROLLING_WINDOW       = 6
        const val MIN_CYCLE_LENGTH     = 15
        const val MAX_CYCLE_LENGTH     = 60
        const val MIN_BLEEDING_DURATION = 1
        const val MAX_BLEEDING_DURATION = 15
    }
}