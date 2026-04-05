package com.lucane.studio.flux.feature.calendar.domain.usecase

import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.repository.DailyLogRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Loads the last 6 months of logs to compute cycle stats.
 * Separate from GetMonthLogsUseCase which only covers the displayed month.
 * 6 months covers at least 3 complete cycles for any average cycle length.
 */
class GetCycleHistoryUseCase @Inject constructor(
    private val repository: DailyLogRepository,
) {
    operator fun invoke(): Flow<List<DailyLog>> {
        val end   = LocalDate.now().plusDays(1)
        val start = end.minusMonths(6)
        return repository.getDailyLogs(startDate = start, endDate = end)
    }
}