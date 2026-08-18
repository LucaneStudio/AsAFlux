package com.lucane.studio.flux.feature.calendar.domain.usecase

import com.lucane.studio.flux.data.model.DailyLog
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/** One recorded cycle: a streak of consecutive days with flow logged. */
data class CycleStreak(
    val start: LocalDate,
    val end: LocalDate,
    val durationDays: Int,   // end - start + 1 (inclusive)
)

/**
 * Groups consecutive period logs (no gap > 1 day) into streaks — each
 * distinct streak is one recorded cycle.
 *
 * Shared by [UpdateCycleStatsUseCase] (rolling averages), [GetCyclePredictionsUseCase]
 * (next period/ovulation/fertile window) and the Home screen's total cycle
 * count, so the definition of "a cycle" lives in exactly one place.
 */
class DetectCycleStreaksUseCase @Inject constructor() {
    operator fun invoke(logs: List<DailyLog>): List<CycleStreak> {
        val sorted = logs
            .filter { it.isPeriod }
            .map { it.date }
            .sorted()

        if (sorted.isEmpty()) return emptyList()

        val streaks = mutableListOf<CycleStreak>()
        var streakStart = sorted.first()
        var streakEnd = sorted.first()

        for (i in 1 until sorted.size) {
            val current = sorted[i]
            val previous = sorted[i - 1]

            if (ChronoUnit.DAYS.between(previous, current) == 1L) {
                // Continuation of the current streak
                streakEnd = current
            } else {
                // Gap detected — close the current streak and open a new one
                streaks += CycleStreak(
                    start = streakStart,
                    end = streakEnd,
                    durationDays = ChronoUnit.DAYS.between(streakStart, streakEnd).toInt() + 1,
                )
                streakStart = current
                streakEnd = current
            }
        }

        // Close the last streak
        streaks += CycleStreak(
            start = streakStart,
            end = streakEnd,
            durationDays = ChronoUnit.DAYS.between(streakStart, streakEnd).toInt() + 1,
        )

        return streaks
    }
}
