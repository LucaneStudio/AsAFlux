package com.lucane.studio.flux.data.repository

import com.lucane.studio.flux.data.model.DailyLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DailyLogRepository {

    /** Reactive stream over a date range — used by the monthly calendar. */
    fun getDailyLogs(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyLog>>

    /** Single read for one day — used by the daily log entry screen. */
    suspend fun getDailyLog(date: LocalDate): DailyLog?

    /** Atomic create-or-update (log + symptoms). */
    suspend fun upsertDailyLog(dailyLog: DailyLog)

    /** Full deletion of a day's log. */
    suspend fun deleteDailyLog(date: LocalDate)

    /**
     * Returns all logs belonging to a declared period, ordered by date ascending.
     * Used by [UpdateCycleStatsUseCase] to detect cycle streaks and compute averages.
     */
    suspend fun getAllPeriodLogs(): List<DailyLog>

    /**
     * Same as [getAllPeriodLogs], as a reactive stream — for UI state that must
     * reflect every write to period logs, not just the ones that also change
     * the DataStore averages.
     */
    fun getAllPeriodLogsFlow(): Flow<List<DailyLog>>

    /**
     * Un-marks period membership strictly after [afterDate] and up to [untilDate].
     * Only the period flag is cleared — any flow intensity, symptoms or notes
     * on those days are preserved, since they may be a genuine declaration
     * independent of the period.
     *
     * Used by [EndPeriodUseCase] to correct days swept into the period range
     * that fall after the actual declared end.
     */
    suspend fun clearPeriodFlagInRange(afterDate: LocalDate, untilDate: LocalDate)

    /**
     * Deletes all logs belonging to a declared period.
     * Must be called at the start of every onboarding save use-case to avoid
     * phantom logs accumulating between successive onboarding runs.
     */
    suspend fun deleteAllPeriodLogs()
}