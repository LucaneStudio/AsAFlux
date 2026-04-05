package com.lucane.studio.flux.app

import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.repository.DailyLogRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Inserts fake cycle data for UI testing.
 * REMOVE before production.
 */
@Singleton
class TestDataSeeder @Inject constructor(
    private val repository: DailyLogRepository,
) {
    suspend fun seed() {
        val existing = repository.getDailyLog(LocalDate.of(2026, 3, 27))
        if (existing != null) {
            android.util.Log.d("TestSeeder", "already seeded, skipping")
            return
        }

        android.util.Log.d("TestSeeder", "seeding...")

        // Cycle 1 — janvier
        listOf(
            LocalDate.of(2026, 1, 30) to FlowIntensity.LIGHT,
            LocalDate.of(2026, 1, 31) to FlowIntensity.MEDIUM,
            LocalDate.of(2026, 2, 1)  to FlowIntensity.HEAVY,
            LocalDate.of(2026, 2, 2)  to FlowIntensity.MEDIUM,
            LocalDate.of(2026, 2, 3)  to FlowIntensity.SPOTTING,
        ).forEach { (date, intensity) ->
            repository.upsertDailyLog(DailyLog(date = date, flowIntensity = intensity))
        }

        // Cycle 2 — février
        listOf(
            LocalDate.of(2026, 2, 27) to FlowIntensity.MEDIUM,
            LocalDate.of(2026, 2, 28) to FlowIntensity.HEAVY,
            LocalDate.of(2026, 3, 1)  to FlowIntensity.HEAVY,
            LocalDate.of(2026, 3, 2)  to FlowIntensity.MEDIUM,
            LocalDate.of(2026, 3, 3)  to FlowIntensity.LIGHT,
        ).forEach { (date, intensity) ->
            repository.upsertDailyLog(DailyLog(date = date, flowIntensity = intensity))
        }

        // Cycle 3 — mars (règles actuelles)
        listOf(
            LocalDate.of(2026, 3, 27) to FlowIntensity.LIGHT,
            LocalDate.of(2026, 3, 28) to FlowIntensity.MEDIUM,
            LocalDate.of(2026, 3, 29) to FlowIntensity.HEAVY,
            LocalDate.of(2026, 3, 30) to FlowIntensity.MEDIUM,
            LocalDate.of(2026, 3, 31) to FlowIntensity.SPOTTING,
        ).forEach { (date, intensity) ->
            repository.upsertDailyLog(DailyLog(date = date, flowIntensity = intensity))
        }
    }
}