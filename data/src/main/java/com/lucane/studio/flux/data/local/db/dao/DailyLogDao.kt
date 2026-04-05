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
}