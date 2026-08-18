package com.lucane.studio.flux.feature.calendar.domain.usecase

import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.model.Symptom
import com.lucane.studio.flux.data.repository.DailyLogRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Saves a full day's declaration (flow, pain, symptoms, note) from the
 * tracking screen, then recalculates cycle stats.
 *
 * ## Recalculating on every save, not just on period end
 * Editing any day — including retroactively, in an already-closed cycle —
 * can change which streaks exist and how long they are, so stats must be
 * rebuilt the same way [EndPeriodUseCase] already does. Cycles declared
 * after the edited day are never rewritten; only the derived averages and
 * predictions shift, which is expected.
 *
 * ## Preserves what this screen doesn't touch
 * [com.lucane.studio.flux.data.model.Mood] isn't part of this screen yet —
 * an existing value for the day is kept as-is rather than wiped.
 */
class LogDayUseCase @Inject constructor(
    private val repository: DailyLogRepository,
    private val updateCycleStats: UpdateCycleStatsUseCase,
) {
    suspend operator fun invoke(
        date: LocalDate,
        flowIntensity: FlowIntensity,
        painLevel: Int?,
        symptoms: List<Symptom>,
        notes: String?,
    ) {
        val existing = repository.getDailyLog(date)
        val toSave = existing?.copy(
            flowIntensity = flowIntensity,
            painLevel = painLevel,
            symptoms = symptoms,
            notes = notes,
        ) ?: DailyLog(
            date = date,
            flowIntensity = flowIntensity,
            painLevel = painLevel,
            symptoms = symptoms,
            notes = notes,
        )

        repository.upsertDailyLog(toSave)
        updateCycleStats()
    }
}
