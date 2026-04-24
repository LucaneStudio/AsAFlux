package com.lucane.studio.flux.feature.onboarding.domain.model

import java.time.LocalDate

data class InitialCycleData(
    val lastPeriodStart: LocalDate,
    val averageCycleLength: Int,
    val averageBleedingDuration: Int,
)