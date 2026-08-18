package com.lucane.studio.flux.data.repository

import androidx.room.withTransaction
import com.lucane.studio.flux.data.local.db.database.AsaFluxDatabase
import com.lucane.studio.flux.data.local.db.entity.DailyLogSymptomCrossRef
import com.lucane.studio.flux.data.mapper.toDomain
import com.lucane.studio.flux.data.mapper.toEntity
import com.lucane.studio.flux.data.model.DailyLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DailyLogRepositoryImpl @Inject constructor(
    private val database: AsaFluxDatabase,
) : DailyLogRepository {

    private val dao get() = database.dailyLogDao()

    override fun getDailyLogs(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyLog>> =
        dao.getDailyLogsWithSymptoms(
            startDate = startDate.toString(),
            endDate   = endDate.toString(),
        ).map { list -> list.map { it.toDomain() } }

    override suspend fun getDailyLog(date: LocalDate): DailyLog? =
        dao.getDailyLogWithSymptoms(date.toString())?.toDomain()

    /**
     * Atomic upsert: deletes existing symptom cross-references first so the
     * symptom list is rebuilt cleanly, then writes the log and new cross-refs
     * within a single transaction.
     */
    override suspend fun upsertDailyLog(dailyLog: DailyLog) {
        database.withTransaction {
            dao.upsertDailyLog(dailyLog.toEntity())
            dao.deleteCrossRefsForDate(dailyLog.date.toString())
            if (dailyLog.symptoms.isNotEmpty()) {
                val crossRefs = dailyLog.symptoms.map { symptom ->
                    DailyLogSymptomCrossRef(
                        date      = dailyLog.date.toString(),
                        symptomId = symptom.id,
                    )
                }
                dao.insertCrossRefs(crossRefs)
            }
        }
    }

    override suspend fun deleteDailyLog(date: LocalDate) =
        dao.deleteDailyLog(date.toString())
    // Cross-refs are removed automatically via CASCADE

    // getAllPeriodLogs returns plain entities (no symptoms join needed —
    // symptoms are not required for cycle stat calculations), mapped via the
    // entity-only mapper to avoid the @Transaction overhead of the relation query.
    override suspend fun getAllPeriodLogs(): List<DailyLog> =
        dao.getAllPeriodLogs().map { it.toDomain() }

    override fun getAllPeriodLogsFlow(): Flow<List<DailyLog>> =
        dao.getAllPeriodLogsFlow().map { list -> list.map { it.toDomain() } }

    override suspend fun clearPeriodFlagInRange(afterDate: LocalDate, untilDate: LocalDate) =
        dao.clearPeriodFlagInRange(
            afterDate = afterDate.toString(),
            untilDate = untilDate.toString(),
        )

    override suspend fun deleteAllPeriodLogs() =
        dao.deleteAllPeriodLogs()
}