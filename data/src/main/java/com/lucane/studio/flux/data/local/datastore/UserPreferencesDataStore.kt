package com.lucane.studio.flux.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private object Keys {
        val DEFAULT_CYCLE_LENGTH           = intPreferencesKey("default_cycle_length")
        val DEFAULT_PERIOD_LENGTH          = intPreferencesKey("default_period_length")
        val PERIOD_REMINDER_ENABLED        = booleanPreferencesKey("period_reminder_enabled")
        val PERIOD_REMINDER_DAYS_BEFORE    = intPreferencesKey("period_reminder_days_before")
        val PERIOD_REMINDER_TIME           = stringPreferencesKey("period_reminder_time")
        val OVULATION_REMINDER_ENABLED     = booleanPreferencesKey("ovulation_reminder_enabled")
        val OVULATION_REMINDER_TIME        = stringPreferencesKey("ovulation_reminder_time")
        val CONTRACEPTION_REMINDER_ENABLED = booleanPreferencesKey("contraception_reminder_enabled")
        val CONTRACEPTION_TYPE             = stringPreferencesKey("contraception_type")
        val CONTRACEPTION_REMINDER_TIME    = stringPreferencesKey("contraception_reminder_time")
        val SHOW_OVULATION      = booleanPreferencesKey("show_ovulation")
        val SHOW_FERTILE_WINDOW = booleanPreferencesKey("show_fertile_window")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            defaultCycleLength           = prefs[Keys.DEFAULT_CYCLE_LENGTH] ?: 28,
            defaultPeriodLength          = prefs[Keys.DEFAULT_PERIOD_LENGTH] ?: 5,
            periodReminderEnabled        = prefs[Keys.PERIOD_REMINDER_ENABLED] ?: false,
            periodReminderDaysBefore     = prefs[Keys.PERIOD_REMINDER_DAYS_BEFORE] ?: 1,
            periodReminderTime           = prefs[Keys.PERIOD_REMINDER_TIME] ?: "20:00",
            ovulationReminderEnabled     = prefs[Keys.OVULATION_REMINDER_ENABLED] ?: false,
            ovulationReminderTime        = prefs[Keys.OVULATION_REMINDER_TIME] ?: "09:00",
            contraceptionReminderEnabled = prefs[Keys.CONTRACEPTION_REMINDER_ENABLED] ?: false,
            contraceptionType            = prefs[Keys.CONTRACEPTION_TYPE],
            contraceptionReminderTime    = prefs[Keys.CONTRACEPTION_REMINDER_TIME] ?: "08:00",
            showOvulation      = prefs[Keys.SHOW_OVULATION] ?: false,
            showFertileWindow  = prefs[Keys.SHOW_FERTILE_WINDOW] ?: false,
        )
    }

    suspend fun updateCycleDefaults(cycleLength: Int, periodLength: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DEFAULT_CYCLE_LENGTH]  = cycleLength
            prefs[Keys.DEFAULT_PERIOD_LENGTH] = periodLength
        }
    }

    suspend fun updatePeriodReminder(enabled: Boolean, daysBefore: Int, time: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.PERIOD_REMINDER_ENABLED]     = enabled
            prefs[Keys.PERIOD_REMINDER_DAYS_BEFORE] = daysBefore
            prefs[Keys.PERIOD_REMINDER_TIME]        = time
        }
    }

    suspend fun updateOvulationReminder(enabled: Boolean, time: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.OVULATION_REMINDER_ENABLED] = enabled
            prefs[Keys.OVULATION_REMINDER_TIME]    = time
        }
    }

    suspend fun updateContraceptionReminder(enabled: Boolean, type: String?, time: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CONTRACEPTION_REMINDER_ENABLED] = enabled
            if (type != null) prefs[Keys.CONTRACEPTION_TYPE] = type
            prefs[Keys.CONTRACEPTION_REMINDER_TIME]    = time
        }
    }

    suspend fun updateShowOvulation(show: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SHOW_OVULATION] = show
        }
    }

    suspend fun updateShowFertileWindow(show: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SHOW_FERTILE_WINDOW] = show
        }
    }
}