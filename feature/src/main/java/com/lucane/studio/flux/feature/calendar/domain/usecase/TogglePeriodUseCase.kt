package com.lucane.studio.flux.feature.calendar.domain.usecase

import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.repository.DailyLogRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Toggles period tracking for a given date.
 *
 * - If no log exists or flow is NONE  → creates/updates with MEDIUM (default start).
 * - If flow is already set            → sets it back to NONE (period ended).
 *
 * The UI can then update the button label based on the resulting state.
 */
class TogglePeriodUseCase @Inject constructor(
    private val repository: DailyLogRepository,
) {
    suspend operator fun invoke(date: LocalDate) {
        val existing = repository.getDailyLog(date)
        val newIntensity = if (
            existing == null || existing.flowIntensity == FlowIntensity.NONE
        ) {
            FlowIntensity.MEDIUM
        } else {
            FlowIntensity.NONE
        }

        val updated = existing?.copy(flowIntensity = newIntensity)
            ?: DailyLog(date = date, flowIntensity = newIntensity)

        repository.upsertDailyLog(updated)
    }
}