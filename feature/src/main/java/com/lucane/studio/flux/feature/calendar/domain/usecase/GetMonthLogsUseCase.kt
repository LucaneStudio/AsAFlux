package com.lucane.studio.flux.feature.calendar.domain.usecase

import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.repository.DailyLogRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

/**
 * Returns a reactive stream of daily logs for the given month.
 * Automatically includes a one-week buffer on each side so the
 * calendar grid (which shows leading/trailing days) has complete data.
 */
class GetMonthLogsUseCase @Inject constructor(
    private val repository: DailyLogRepository,
) {
    operator fun invoke(yearMonth: YearMonth): Flow<List<DailyLog>> {
        val start = yearMonth.atDay(1).minusWeeks(1)
        val end   = yearMonth.atEndOfMonth().plusWeeks(1)
        return repository.getDailyLogs(startDate = start, endDate = end)
    }
}