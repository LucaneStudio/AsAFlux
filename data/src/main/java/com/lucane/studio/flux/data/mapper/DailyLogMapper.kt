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
)

fun DailyLog.toEntity(): DailyLogEntity = DailyLogEntity(
    date          = date.toString(),
    flowIntensity = flowIntensity.name,
    painLevel     = painLevel,
    mood          = mood?.name,
    notes         = notes,
)