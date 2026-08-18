package com.lucane.studio.flux.data.model

import java.time.LocalDate

data class DailyLog(
    val date: LocalDate,
    val flowIntensity: FlowIntensity,
    val painLevel: Int? = null,
    val mood: Mood? = null,
    val notes: String? = null,
    val symptoms: List<Symptom> = emptyList(),
    /**
     * True if this day belongs to a declared bleeding period (via start/end
     * period, or auto-confirmed). Independent from [flowIntensity]: a day can
     * have a declared flow intensity (e.g. spotting) without being part of a
     * period, and a period day can exist without a specific intensity
     * ([FlowIntensity.NOT_DECLARED]).
     */
    val isPeriod: Boolean = false,
)