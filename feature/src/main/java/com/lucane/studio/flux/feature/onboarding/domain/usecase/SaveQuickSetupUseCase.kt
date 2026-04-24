package com.lucane.studio.flux.feature.onboarding.domain.usecase

import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.repository.DailyLogRepository
import com.lucane.studio.flux.feature.onboarding.domain.model.InitialCycleData
import javax.inject.Inject

class SaveQuickSetupUseCase @Inject constructor(
    private val dailyLogRepository: DailyLogRepository,
    private val settingsDataStore: SettingsDataStore,
) {
    suspend operator fun invoke(data: InitialCycleData) {
        settingsDataStore.setAverageCycleLength(data.averageCycleLength)
        settingsDataStore.setAverageBleedingDuration(data.averageBleedingDuration)
        repeat(data.averageBleedingDuration) { offset ->
            val date = data.lastPeriodStart.plusDays(offset.toLong())
            val intensity = when (offset) {
                0                              -> FlowIntensity.LIGHT
                data.averageBleedingDuration-1 -> FlowIntensity.LIGHT
                else                           -> FlowIntensity.MEDIUM
            }
            dailyLogRepository.upsertDailyLog(DailyLog(date = date, flowIntensity = intensity))
        }
    }
}