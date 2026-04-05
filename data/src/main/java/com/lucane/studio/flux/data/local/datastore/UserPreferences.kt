package com.lucane.studio.flux.data.local.datastore

/**
 * User preferences model.
 *
 * Notification keys are already declared here (Phase 3 anticipation)
 * but their features will be enabled progressively.
 */
data class UserPreferences(
    // Cycle defaults
    val defaultCycleLength: Int = 28,
    val defaultPeriodLength: Int = 5,
    // Period reminder
    val periodReminderEnabled: Boolean = false,
    val periodReminderDaysBefore: Int = 1,
    val periodReminderTime: String = "20:00",
    // Ovulation reminder
    val ovulationReminderEnabled: Boolean = false,
    val ovulationReminderTime: String = "09:00",
    // Contraception reminder
    val contraceptionReminderEnabled: Boolean = false,
    val contraceptionType: String? = null,
    val contraceptionReminderTime: String = "08:00",
    val showOvulation: Boolean = false,
    val showFertileWindow: Boolean = false,
)