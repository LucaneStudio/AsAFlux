package com.lucane.studio.flux.feature.onboarding.domain.usecase

import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.repository.DailyLogRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.roundToInt

class SaveHistoricalCyclesUseCase @Inject constructor(
    private val dailyLogRepository: DailyLogRepository,
    private val settingsDataStore: SettingsDataStore,
) {
    suspend operator fun invoke(startDates: List<LocalDate>, bleedingDuration: Int) {
        require(startDates.size >= 3) { "Au moins 3 cycles requis" }

        val sorted = startDates.sorted()
        val avgCycleLength = sorted
            .zipWithNext { a, b -> ChronoUnit.DAYS.between(a, b).toInt() }
            .average()
            .roundToInt()
            .coerceIn(15, 60)

        settingsDataStore.setAverageCycleLength(avgCycleLength)
        settingsDataStore.setAverageBleedingDuration(bleedingDuration)

        sorted.forEach { startDate ->
            repeat(bleedingDuration) { offset ->
                val date = startDate.plusDays(offset.toLong())
                val intensity = when (offset) {
                    0                  -> FlowIntensity.LIGHT
                    bleedingDuration-1 -> FlowIntensity.LIGHT
                    else               -> FlowIntensity.MEDIUM
                }
                dailyLogRepository.upsertDailyLog(DailyLog(date = date, flowIntensity = intensity))
            }
        }
    }
}