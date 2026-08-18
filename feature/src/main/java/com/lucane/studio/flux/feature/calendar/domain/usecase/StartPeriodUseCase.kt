package com.lucane.studio.flux.feature.calendar.domain.usecase

import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.repository.DailyLogRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

/**
 * Records the start of a bleeding period on the given date, then pre-fills
 * the following days up to the average bleeding duration.
 *
 * ## Declared vs not declared
 * - The start date is saved with [FlowIntensity.MEDIUM] — the user explicitly
 *   tapped "start of bleeding", which is a positive declaration.
 * - The following pre-filled days are saved with [FlowIntensity.NOT_DECLARED]:
 *   we know a period is ongoing, but we must not assert a specific intensity
 *   the user has not confirmed. This is critical for data exported to a
 *   healthcare professional — a false intensity is worse than no intensity.
 *
 * ## Pre-fill is non-destructive
 * Days that already carry an explicit flow entry (anything other than
 * [FlowIntensity.NONE] or [FlowIntensity.NOT_DECLARED]) are left untouched.
 * The user's prior declarations always take priority.
 *
 * ## User is the source of truth
 * Whatever date is declared is persisted as-is, whether today, yesterday,
 * or any other date. Individual days can be corrected from the calendar page.
 *
 * ## isPeriod vs flowIntensity
 * [DailyLog.isPeriod] is always forced true across the whole range — that's
 * the actual "this day is part of a period" signal used by cycle stats and
 * predictions. [FlowIntensity] stays a separate, editable declaration: a day
 * can carry a specific intensity without being a period day (e.g. spotting),
 * so an existing explicit intensity is preserved rather than overwritten.
 */
class StartPeriodUseCase @Inject constructor(
    private val repository: DailyLogRepository,
    private val settingsDataStore: SettingsDataStore,
) {
    suspend operator fun invoke(date: LocalDate) {
        val bleedingDuration = settingsDataStore.averageBleedingDuration.first()

        // Day 0 — declared start: explicitly set to MEDIUM, always marked as period
        val existingStart = repository.getDailyLog(date)
        val startLog = existingStart?.copy(flowIntensity = FlowIntensity.MEDIUM, isPeriod = true)
            ?: DailyLog(date = date, flowIntensity = FlowIntensity.MEDIUM, isPeriod = true)
        repository.upsertDailyLog(startLog)

        // Days 1 … (bleedingDuration - 1) — always marked as period; the flow
        // intensity itself is pre-filled as NOT_DECLARED, non-destructively.
        for (offset in 1 until bleedingDuration) {
            val day         = date.plusDays(offset.toLong())
            val existingDay = repository.getDailyLog(day)

            // An explicit user declaration keeps its intensity — only period
            // membership is forced on top of it.
            val hasExplicitIntensity = existingDay != null
                && existingDay.flowIntensity != FlowIntensity.NONE
                && existingDay.flowIntensity != FlowIntensity.NOT_DECLARED

            val dayLog = when {
                existingDay == null  -> DailyLog(date = day, flowIntensity = FlowIntensity.NOT_DECLARED, isPeriod = true)
                hasExplicitIntensity -> existingDay.copy(isPeriod = true)
                else                 -> existingDay.copy(flowIntensity = FlowIntensity.NOT_DECLARED, isPeriod = true)
            }
            repository.upsertDailyLog(dayLog)
        }
    }
}