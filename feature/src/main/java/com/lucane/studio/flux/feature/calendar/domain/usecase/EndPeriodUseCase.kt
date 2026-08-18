package com.lucane.studio.flux.feature.calendar.domain.usecase

import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.repository.DailyLogRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Records the end of the current bleeding period on the given date.
 *
 * ## What this use-case does
 * 1. Ensures the declared end date is marked as a period day and carries a
 *    flow entry (preserves existing intensity if already set, defaults to
 *    [FlowIntensity.MEDIUM] otherwise).
 * 2. Un-marks period membership for any day strictly after [date] within a
 *    30-day window — this corrects days the user may have swept into the
 *    period previously but that are now beyond the declared end. Their flow
 *    intensity, symptoms and notes are left untouched — only [DailyLog.isPeriod]
 *    is cleared, since that data may be a genuine declaration independent of
 *    the period (e.g. spotting).
 * 3. Triggers [UpdateCycleStatsUseCase] so cycle averages are recalculated
 *    immediately after the period is closed.
 *
 * ## The user is the source of truth
 * If the user declares the period ended on a past date, that date is trusted
 * as-is. No automatic inference or adjustment is made.
 *
 * ## Cleanup window
 * The window is [date + 1 day, date + 30 days]. 30 days is a safe upper bound
 * because no bleeding period lasts longer than that.
 */
class EndPeriodUseCase @Inject constructor(
    private val repository: DailyLogRepository,
    private val updateCycleStats: UpdateCycleStatsUseCase,
) {

    companion object {
        /** Maximum number of days to scan after the declared end date for cleanup. */
        private const val CLEANUP_WINDOW_DAYS = 30L
    }

    suspend operator fun invoke(date: LocalDate) {
        // 1. Ensure the end date itself has a flow log and is marked as period.
        val existing = repository.getDailyLog(date)
        val toSave = when {
            existing == null                             -> com.lucane.studio.flux.data.model.DailyLog(
                date          = date,
                flowIntensity = FlowIntensity.MEDIUM,
                isPeriod      = true,
            )
            existing.flowIntensity == FlowIntensity.NONE -> existing.copy(
                flowIntensity = FlowIntensity.MEDIUM,
                isPeriod      = true,
            )
            else                                          -> existing.copy(isPeriod = true) // already has flow — keep it
        }
        repository.upsertDailyLog(toSave)

        // 2. Correct any days swept into the period beyond the declared end date.
        repository.clearPeriodFlagInRange(
            afterDate = date,
            untilDate = date.plusDays(CLEANUP_WINDOW_DAYS),
        )

        // 3. Recalculate cycle statistics now that a period is complete.
        updateCycleStats()
    }
}