package com.lucane.studio.flux.feature.calendar.domain.usecase

import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.repository.DailyLogRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

/**
 * Silently confirms a predicted period the user never declared, once its
 * estimated end is [GRACE_PERIOD_DAYS] days in the past.
 *
 * ## Why
 * Cycle stats and predictions are built by walking consecutive recorded
 * streaks — a period the user simply forgot to log breaks that chain and
 * stalls every future prediction. Assuming the estimate was correct keeps
 * the chain going; it's a safer default than leaving a silent gap.
 *
 * ## Why NOT_DECLARED, not a declared intensity
 * Nothing here was confirmed by the user — not even that the period
 * happened at all, only that the estimate was never contradicted. Per
 * [FlowIntensity.NOT_DECLARED], asserting a specific intensity the user
 * never provided would be worse than not having the data, especially for
 * anything exported to a healthcare professional.
 *
 * ## Always correctable
 * Every day written here is a normal [DailyLog] the user can edit from the
 * calendar screen like any other — this is a starting guess, not a lock.
 *
 * ## Catch-up
 * Runs once at app startup (see [com.lucane.studio.flux.app.MainViewModel]).
 * If the app wasn't opened for several missed cycles in a row, confirms
 * each of them in turn (bounded by [MAX_CATCH_UP_CYCLES]) so a long absence
 * doesn't leave stats stuck on a single stale prediction.
 */
class AutoConfirmMissedPeriodUseCase @Inject constructor(
    private val repository: DailyLogRepository,
    private val settingsDataStore: SettingsDataStore,
    private val getCycleHistory: GetCycleHistoryUseCase,
    private val getCyclePredictions: GetCyclePredictionsUseCase,
    private val updateCycleStats: UpdateCycleStatsUseCase,
) {
    companion object {
        private const val GRACE_PERIOD_DAYS = 5L
        private const val MAX_CATCH_UP_CYCLES = 24
    }

    suspend operator fun invoke() {
        repeat(MAX_CATCH_UP_CYCLES) {
            val confirmedOne = confirmNextMissedPeriodIfOverdue()
            if (!confirmedOne) return
        }
    }

    /** Returns true if a missed period was confirmed (caller should re-check for another). */
    private suspend fun confirmNextMissedPeriodIfOverdue(): Boolean {
        val history = getCycleHistory().first()
        val avgCycleLength = settingsDataStore.averageCycleLength.first()
        val avgBleedingDuration = settingsDataStore.averageBleedingDuration.first()

        val predictions = getCyclePredictions(history, avgCycleLength, avgBleedingDuration)
        val predictedStart = predictions.nextPeriodStart ?: return false
        val predictedEnd = predictions.nextPeriodEnd ?: return false

        val confirmAfter = predictedEnd.plusDays(GRACE_PERIOD_DAYS)
        if (LocalDate.now().isBefore(confirmAfter)) return false

        // Already declared (start/end tapped, or edited manually)? Nothing to do.
        val alreadyDeclared = history.any { log ->
            !log.date.isBefore(predictedStart) && !log.date.isAfter(predictedEnd) && log.isPeriod
        }
        if (alreadyDeclared) return false

        var day = predictedStart
        while (!day.isAfter(predictedEnd)) {
            val existing = repository.getDailyLog(day)

            // An explicit user declaration keeps its intensity — only period
            // membership is forced on top of it, same rule as StartPeriodUseCase.
            val hasExplicitIntensity = existing != null
                && existing.flowIntensity != FlowIntensity.NONE
                && existing.flowIntensity != FlowIntensity.NOT_DECLARED

            val toSave = when {
                existing == null     -> DailyLog(date = day, flowIntensity = FlowIntensity.NOT_DECLARED, isPeriod = true)
                hasExplicitIntensity -> existing.copy(isPeriod = true)
                else                 -> existing.copy(flowIntensity = FlowIntensity.NOT_DECLARED, isPeriod = true)
            }
            repository.upsertDailyLog(toSave)
            day = day.plusDays(1)
        }

        updateCycleStats()
        return true
    }
}
