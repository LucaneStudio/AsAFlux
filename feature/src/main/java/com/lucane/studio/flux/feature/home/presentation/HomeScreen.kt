package com.lucane.studio.flux.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucane.studio.flux.feature.calendar.presentation.CalendarScreen
import com.lucane.studio.flux.feature.home.presentation.components.CyclePhaseEntry
import com.lucane.studio.flux.feature.home.presentation.components.CyclePhaseSection
import com.lucane.studio.flux.feature.home.presentation.components.DailySensationSection
import com.lucane.studio.flux.feature.home.presentation.components.MyCyclesSection
import com.lucane.studio.flux.core.R as CoreRes

/**
 * Home tab. Hosts the calendar view that used to live on the Calendar tab —
 * the Calendar tab is getting its own distinct layout from design, not yet
 * provided, so it stays blank until then (see [com.lucane.studio.flux.app.presentation.screen.MainScreen]).
 *
 * "Sensation du jour" is a shortcut to the (not yet built) symptom-logging
 * screen — left static/inert on purpose, wiring it is a separate task.
 */
// TODO DA: real data now, still pending the final visual design pass
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CalendarScreen()

        DailySensationSection()

        when (val state = uiState) {
            is HomeUiState.Loading -> {
                // TODO: skeleton / shimmer
            }

            is HomeUiState.Error -> {
                // TODO: error state composable
            }

            is HomeUiState.Success -> {
                CyclePhaseSection(
                    entries = state.cyclePhaseEntries.map { prediction ->
                        val (labelRes, iconRes) = when (prediction.type) {
                            CyclePhaseType.PERIOD    -> CoreRes.string.cycle_phase_period_upcoming to CoreRes.drawable.ic_period
                            CyclePhaseType.FERTILE   -> CoreRes.string.cycle_phase_fertile_upcoming to CoreRes.drawable.ic_fertility
                            CyclePhaseType.OVULATION -> CoreRes.string.cycle_phase_ovulation_upcoming to CoreRes.drawable.ic_ovum
                        }
                        CyclePhaseEntry(
                            dateLabel  = prediction.dateLabel,
                            phaseLabel = labelRes,
                            iconRes    = iconRes,
                        )
                    },
                )

                MyCyclesSection(
                    cyclesEnteredCount          = state.totalCyclesRecorded,
                    averageBleedingDurationDays = state.averageBleedingDurationDays,
                    averageCycleLengthDays      = state.averageCycleLengthDays,
                )
            }
        }
    }
}
