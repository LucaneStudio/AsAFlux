package com.lucane.studio.flux.feature.home.domain.usecase

import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import com.lucane.studio.flux.data.repository.DailyLogRepository
import com.lucane.studio.flux.feature.calendar.domain.usecase.DetectCycleStreaksUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/** All-time cycle overview for the Home screen's "My cycles" section. */
data class CycleOverview(
    val totalCyclesRecorded: Int,
    val averageBleedingDurationDays: Int,
    val averageCycleLengthDays: Int,
)

/**
 * Reactive [CycleOverview]. The total count spans every recorded cycle ever
 * (not just the recent window used for predictions), matching the scope
 * [com.lucane.studio.flux.feature.calendar.domain.usecase.UpdateCycleStatsUseCase]
 * already uses for its own rolling averages.
 */
class GetCycleOverviewUseCase @Inject constructor(
    private val repository: DailyLogRepository,
    private val settingsDataStore: SettingsDataStore,
    private val detectCycleStreaks: DetectCycleStreaksUseCase,
) {
    operator fun invoke(): Flow<CycleOverview> = combine(
        repository.getAllPeriodLogsFlow(),
        settingsDataStore.averageCycleLength,
        settingsDataStore.averageBleedingDuration,
    ) { allPeriodLogs, cycleLength, bleedingDuration ->
        CycleOverview(
            totalCyclesRecorded = detectCycleStreaks(allPeriodLogs).size,
            averageBleedingDurationDays = bleedingDuration,
            averageCycleLengthDays = cycleLength,
        )
    }
}
