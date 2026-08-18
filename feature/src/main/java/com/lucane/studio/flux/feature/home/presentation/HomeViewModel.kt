package com.lucane.studio.flux.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucane.studio.flux.data.model.DailyLog
import com.lucane.studio.flux.feature.calendar.domain.usecase.GetCycleHistoryUseCase
import com.lucane.studio.flux.feature.calendar.domain.usecase.GetCyclePredictionsUseCase
import com.lucane.studio.flux.feature.home.domain.usecase.CycleOverview
import com.lucane.studio.flux.feature.home.domain.usecase.GetCycleOverviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCycleHistory: GetCycleHistoryUseCase,
    private val getCyclePredictions: GetCyclePredictionsUseCase,
    private val getCycleOverview: GetCycleOverviewUseCase,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        getCycleHistory(),
        getCycleOverview(),
    ) { history, overview ->
        buildUiState(history, overview)
    }
        .catch { e -> emit(HomeUiState.Error(e.message ?: "Unknown error")) }
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading,
        )

    private fun buildUiState(history: List<DailyLog>, overview: CycleOverview): HomeUiState {
        val predictions = getCyclePredictions(
            history             = history,
            avgCycleLength      = overview.averageCycleLengthDays,
            avgBleedingDuration = overview.averageBleedingDurationDays,
        )

        // Listed chronologically (fertile window opens first, then ovulation,
        // then the next period ~14 days later) rather than by category —
        // this reads as an upcoming-events timeline, soonest first.
        val dateFormatter = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())
        val entries = buildList {
            predictions.fertileWindowStart?.let {
                add(CyclePhasePrediction(it.format(dateFormatter), CyclePhaseType.FERTILE))
            }
            predictions.ovulationDate?.let {
                add(CyclePhasePrediction(it.format(dateFormatter), CyclePhaseType.OVULATION))
            }
            predictions.nextPeriodStart?.let {
                add(CyclePhasePrediction(it.format(dateFormatter), CyclePhaseType.PERIOD))
            }
            predictions.periodAfterNextStart?.let {
                add(CyclePhasePrediction(it.format(dateFormatter), CyclePhaseType.PERIOD))
            }
        }

        return HomeUiState.Success(
            cyclePhaseEntries           = entries,
            totalCyclesRecorded         = overview.totalCyclesRecorded,
            averageBleedingDurationDays = overview.averageBleedingDurationDays,
            averageCycleLengthDays      = overview.averageCycleLengthDays,
        )
    }
}
