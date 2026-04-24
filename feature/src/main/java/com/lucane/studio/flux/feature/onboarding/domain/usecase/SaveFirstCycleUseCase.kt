package com.lucane.studio.flux.feature.onboarding.domain.usecase

import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.repository.DailyLogRepository
import java.time.LocalDate
import javax.inject.Inject

class SaveFirstCycleUseCase @Inject constructor(
    private val dailyLogRepository: DailyLogRepository,
    private val settingsDataStore: SettingsDataStore,
) {
    suspend operator fun invoke(startDate: LocalDate) {
        settingsDataStore.setAverageCycleLength(28)
        settingsDataStore.setAverageBleedingDuration(5)
        dailyLogRepository.upsertDailyLog(
            DailyLog(date = startDate, flowIntensity = FlowIntensity.MEDIUM)
        )
    }
}