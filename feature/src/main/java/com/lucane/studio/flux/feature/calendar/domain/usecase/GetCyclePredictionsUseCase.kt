package com.lucane.studio.flux.feature.calendar.domain.usecase

import com.lucane.studio.flux.data.model.DailyLog
import java.time.LocalDate
import javax.inject.Inject

/**
 * Predicted dates for upcoming cycle events. Null fields mean "not enough
 * data yet" — no cycle has been recorded in the supplied history.
 */
data class CyclePredictions(
    val nextPeriodStart: LocalDate?,
    val nextPeriodEnd: LocalDate?,
    val ovulationDate: LocalDate?,
    val fertileWindowStart: LocalDate?,
    val fertileWindowEnd: LocalDate?,
    val periodAfterNextStart: LocalDate?,
)

/**
 * Derives [CyclePredictions] from recent cycle [history] and the current
 * average cycle length / bleeding duration.
 *
 * ## 6-cycle rule
 * [avgCycleLength] and [avgBleedingDuration] already reflect either the
 * onboarding value (< 6 recorded cycles) or the computed rolling average
 * (6+), written by [UpdateCycleStatsUseCase] — this use case only applies
 * whichever value it's given, it doesn't decide which one is "correct".
 */
class GetCyclePredictionsUseCase @Inject constructor(
    private val detectCycleStreaks: DetectCycleStreaksUseCase,
) {
    operator fun invoke(
        history: List<DailyLog>,
        avgCycleLength: Int,
        avgBleedingDuration: Int,
    ): CyclePredictions {
        val lastPeriodStart = detectCycleStreaks(history).lastOrNull()?.start
            ?: return CyclePredictions(null, null, null, null, null, null)

        val nextPeriodStart = lastPeriodStart.plusDays(avgCycleLength.toLong())
        val ovulationDate = nextPeriodStart.minusDays(14)

        return CyclePredictions(
            nextPeriodStart = nextPeriodStart,
            nextPeriodEnd = nextPeriodStart.plusDays(avgBleedingDuration.toLong() - 1),
            ovulationDate = ovulationDate,
            fertileWindowStart = ovulationDate.minusDays(5),
            fertileWindowEnd = ovulationDate.plusDays(1),
            periodAfterNextStart = nextPeriodStart.plusDays(avgCycleLength.toLong()),
        )
    }
}
