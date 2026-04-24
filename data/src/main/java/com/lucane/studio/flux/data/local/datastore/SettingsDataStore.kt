package com.lucane.studio.flux.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        // ── nouveau ──────────────────────────────────
        val ONBOARDING_COMPLETED      = booleanPreferencesKey("onboarding_completed")
        val AVERAGE_CYCLE_LENGTH      = intPreferencesKey("average_cycle_length")
        val AVERAGE_BLEEDING_DURATION = intPreferencesKey("average_bleeding_duration")
        // ── à compléter avec vos clés existantes ─────
    }

    // ── Onboarding ───────────────────────────────────

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { it[Keys.ONBOARDING_COMPLETED] ?: false }

    suspend fun setOnboardingCompleted(value: Boolean) {
        context.dataStore.edit { it[Keys.ONBOARDING_COMPLETED] = value }
    }

    // ── Cycle preferences ────────────────────────────

    val averageCycleLength: Flow<Int> = context.dataStore.data
        .map { it[Keys.AVERAGE_CYCLE_LENGTH] ?: 28 }

    suspend fun setAverageCycleLength(value: Int) {
        context.dataStore.edit { it[Keys.AVERAGE_CYCLE_LENGTH] = value }
    }

    val averageBleedingDuration: Flow<Int> = context.dataStore.data
        .map { it[Keys.AVERAGE_BLEEDING_DURATION] ?: 5 }

    suspend fun setAverageBleedingDuration(value: Int) {
        context.dataStore.edit { it[Keys.AVERAGE_BLEEDING_DURATION] = value }
    }
}