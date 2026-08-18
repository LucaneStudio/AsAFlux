package com.lucane.studio.flux.feature.home.presentation

/** Which kind of cycle event a [CyclePhasePrediction] represents. */
enum class CyclePhaseType {
    PERIOD,
    FERTILE,
    OVULATION,
}

/** One upcoming event for the "Phase du cycle" section, date already formatted. */
data class CyclePhasePrediction(
    val dateLabel: String,
    val type: CyclePhaseType,
)

sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Error(val message: String) : HomeUiState

    data class Success(
        val cyclePhaseEntries: List<CyclePhasePrediction>,
        val totalCyclesRecorded: Int,
        val averageBleedingDurationDays: Int,
        val averageCycleLengthDays: Int,
    ) : HomeUiState
}
