package com.lucane.studio.flux.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Many-to-many join table between [DailyLogEntity] and [SymptomEntity].
 *
 * Both foreign keys use CASCADE DELETE: removing a log or a symptom
 * automatically cleans up the associated rows in this table.
 */
@Entity(
    tableName = "daily_log_symptoms",
    primaryKeys = ["date", "symptomId"],
    foreignKeys = [
        ForeignKey(
            entity = DailyLogEntity::class,
            parentColumns = ["date"],
            childColumns = ["date"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SymptomEntity::class,
            parentColumns = ["id"],
            childColumns = ["symptomId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("symptomId")],
)
data class DailyLogSymptomCrossRef(
    val date: String,
    val symptomId: Long,
)