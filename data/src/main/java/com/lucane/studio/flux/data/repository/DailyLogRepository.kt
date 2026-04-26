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

    suspend fun deleteAllPeriodLogs()
}