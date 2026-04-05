package com.lucane.studio.flux.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for a daily log entry.
 *
 * Primary key is the date stored as an ISO-8601 string (e.g. "2024-01-09").
 * Enums are persisted as their [Enum.name] string: human-readable in the DB
 * and safe against enum reordering.
 *
 * ⚠️ Never rename an enum value without a corresponding Room migration
 * that updates the stored strings via an UPDATE statement.
 */
@Entity(tableName = "daily_logs")
data class DailyLogEntity(
    @PrimaryKey val date: String,
    val flowIntensity: String,   // FlowIntensity.name
    val painLevel: Int?,
    val mood: String?,           // Mood.name or null
    val notes: String?,
)