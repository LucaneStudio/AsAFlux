package com.lucane.studio.flux.data.mapper

import com.lucane.studio.flux.data.local.db.entity.DailyLogEntity
import com.lucane.studio.flux.data.local.db.relation.DailyLogWithSymptoms
import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.model.Mood
import java.time.LocalDate

fun DailyLogWithSymptoms.toDomain(): DailyLog = DailyLog(
    date          = LocalDate.parse(dailyLog.date),
    flowIntensity = FlowIntensity.valueOf(dailyLog.flowIntensity),
    painLevel     = dailyLog.painLevel,
    mood          = dailyLog.mood?.let { Mood.valueOf(it) },
    notes         = dailyLog.notes,
    symptoms      = symptoms.map { it.toDomain() },
    isPeriod      = dailyLog.isPeriod,
)

/**
 * Maps a bare entity (no symptom join) to the domain model.
 * Used on read paths where symptoms are not needed — e.g. cycle stat
 * calculations — to avoid the @Transaction overhead of the relation query.
 */
fun DailyLogEntity.toDomain(): DailyLog = DailyLog(
    date          = LocalDate.parse(date),
    flowIntensity = FlowIntensity.valueOf(flowIntensity),
    painLevel     = painLevel,
    mood          = mood?.let { Mood.valueOf(it) },
    notes         = notes,
    symptoms      = emptyList(),
    isPeriod      = isPeriod,
)

fun DailyLog.toEntity(): DailyLogEntity = DailyLogEntity(
    date          = date.toString(),
    flowIntensity = flowIntensity.name,
    painLevel     = painLevel,
    mood          = mood?.name,
    notes         = notes,
    isPeriod      = isPeriod,
)