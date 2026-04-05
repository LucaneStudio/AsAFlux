package com.lucane.studio.flux.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for a symptom.
 * Custom symptoms (isCustom = true) are user-defined entries.
 */
@Entity(tableName = "symptoms")
data class SymptomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val category: String,    // SymptomCategory.name
    val isCustom: Boolean,
)