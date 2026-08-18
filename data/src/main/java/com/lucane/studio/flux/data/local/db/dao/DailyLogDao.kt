package com.lucane.studio.flux.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.lucane.studio.flux.data.local.db.entity.DailyLogEntity
import com.lucane.studio.flux.data.local.db.entity.DailyLogSymptomCrossRef
import com.lucane.studio.flux.data.local.db.relation.DailyLogWithSymptoms
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyLogDao {

    // ─── Read ─────────────────────────────────────────────────────────────────

    @Transaction
    @Query(
        """
        SELECT * FROM daily_logs
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date ASC
        """
    )
    fun getDailyLogsWithSymptoms(
        startDate: String,
        endDate: String,
    ): Flow<List<DailyLogWithSymptoms>>

    @Transaction
    @Query("SELECT * FROM daily_logs WHERE date = :date")
    suspend fun getDailyLogWithSymptoms(date: String): DailyLogWithSymptoms?

    /**
     * Returns all logs belonging to a declared period, ordered by date ascending.
     * Used by [UpdateCycleStatsUseCase] to count recorded cycles and compute averages.
     * Suspend (non-reactive) — called once per stat recalculation, not observed.
     */
    @Query("SELECT * FROM daily_logs WHERE isPeriod = 1 ORDER BY date ASC")
    suspend fun getAllPeriodLogs(): List<DailyLogEntity>

    /**
     * Same query as [getAllPeriodLogs], as a reactive stream. Used by
     * GetCycleOverviewUseCase's total-cycle count, which must reflect every
     * daily_logs write (e.g. from AutoConfirmMissedPeriodUseCase) — not only
     * the ones that happen to also change the DataStore averages.
     */
    @Query("SELECT * FROM daily_logs WHERE isPeriod = 1 ORDER BY date ASC")
    fun getAllPeriodLogsFlow(): Flow<List<DailyLogEntity>>

    // ─── Write ────────────────────────────────────────────────────────────────

    @Upsert
    suspend fun upsertDailyLog(entity: DailyLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefs(crossRefs: List<DailyLogSymptomCrossRef>)

    // ─── Delete ───────────────────────────────────────────────────────────────

    @Query("DELETE FROM daily_logs WHERE date = :date")
    suspend fun deleteDailyLog(date: String)

    /**
     * Deletes all symptom cross-references for a given date.
     * Called before every upsert so the symptom list is rebuilt
     * from scratch, avoiding stale or duplicate entries.
     */
    @Query("DELETE FROM daily_log_symptoms WHERE date = :date")
    suspend fun deleteCrossRefsForDate(date: String)

    /**
     * Un-marks period membership for days strictly after [afterDate] and up
     * to [untilDate] (both bounds exclusive / inclusive respectively).
     *
     * Used by [EndPeriodUseCase] to correct days that were previously
     * swept into the period range but fall after the actual declared end.
     * Only [DailyLogEntity.isPeriod] is cleared — any flow intensity,
     * symptoms or notes on those days are left untouched, since they may be
     * a genuine declaration independent of the period (e.g. spotting).
     */
    @Query(
        """
        UPDATE daily_logs
        SET isPeriod = 0
        WHERE date > :afterDate
          AND date <= :untilDate
          AND isPeriod = 1
        """
    )
    suspend fun clearPeriodFlagInRange(afterDate: String, untilDate: String)

    /**
     * Deletes all logs belonging to a declared period. Called at the start of
     * every onboarding save use-case to avoid phantom logs accumulating
     * between two successive onboarding runs (safe there: onboarding always
     * runs before any real tracking data exists).
     *
     * Logs with isPeriod = false (notes-only, symptom-only, or independently
     * declared flow) are preserved because the user may have entered them.
     */
    @Query("DELETE FROM daily_logs WHERE isPeriod = 1")
    suspend fun deleteAllPeriodLogs()
}