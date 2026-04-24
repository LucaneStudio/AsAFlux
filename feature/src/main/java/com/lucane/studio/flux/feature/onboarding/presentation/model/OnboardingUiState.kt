package com.lucane.studio.flux.feature.onboarding.presentation.model

import java.time.LocalDate

data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.Disclaimer,
    val selectedMode: OnboardingMode? = null,

    // Mode FIRST_CYCLE
    val firstCycleDate: LocalDate? = null,

    // Mode QUICK
    val lastPeriodDate: LocalDate? = null,
    val cycleLengthInput: String = "28",
    val bleedingDurationInput: String = "5",

    // Mode HISTORY — 6 slots, null = vide
    val cycleHistory: List<LocalDate?> = List(6) { null },
    val bleedingDurationHistoryInput: String = "5",

    val isSaving: Boolean = false,
    val isComplete: Boolean = false,
    val errorMessage: String? = null,
) {
    // Validation quick
    val isQuickSetupValid: Boolean
        get() = lastPeriodDate != null
                && cycleLengthInput.toIntOrNull()?.let { it in 15..60 } == true
                && bleedingDurationInput.toIntOrNull()?.let { it in 1..15 } == true

    val filledHistoryCount: Int
        get() = cycleHistory.count { it != null }

    val isHistorySetupValid: Boolean
        get() = filledHistoryCount >= 3
                && bleedingDurationHistoryInput.toIntOrNull()?.let { it in 1..15 } == true

    val estimatedCycleLength: Int?
        get() {
            val sorted = cycleHistory.filterNotNull().sorted()
            if (sorted.size < 2) return null
            val lengths = sorted.zipWithNext { a, b ->
                java.time.temporal.ChronoUnit.DAYS.between(a, b).toInt()
            }
            return lengths.average().toInt().coerceIn(15, 60)
        }
}