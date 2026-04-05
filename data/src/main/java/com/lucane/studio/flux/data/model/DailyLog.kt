package com.lucane.studio.flux.data.model

import java.time.LocalDate
import java.util.Collections.emptyList

data class DailyLog(
    val date: LocalDate,
    val flowIntensity: FlowIntensity,
    val painLevel: Int? = null,
    val mood: Mood? = null,
    val notes: String? = null,
    val symptoms: List<Symptom> = emptyList(),
)