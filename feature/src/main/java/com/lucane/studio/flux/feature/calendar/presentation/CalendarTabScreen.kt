package com.lucane.studio.flux.feature.calendar.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucane.studio.flux.core.providers.LocalHazeController
import com.lucane.studio.flux.core.ui.cards.CardBase
import com.lucane.studio.flux.feature.calendar.presentation.components.CalendarLegend
import com.lucane.studio.flux.feature.calendar.presentation.components.SymptomDeclarationCard
import com.lucane.studio.flux.feature.calendar.presentation.components.TrackingCalendarGrid
import com.lucane.studio.flux.feature.calendar.presentation.components.TrackingCalendarHeader

/**
 * Calendar tab ("Suivi") screen — any day is selectable (not just today),
 * and the panel below lets the user declare/edit that day's flow, symptoms,
 * pain and note, including retroactively.
 *
 * Uses the exact same [CalendarViewModel] instance as the Home tab's compact
 * calendar widget (both resolve within the same nav back-stack entry via
 * [hiltViewModel]), so the two calendars can never drift out of sync — this
 * screen only adds day-selection and the declaration panel on top of it.
 */
// TODO DA: layout placeholder — pending the final visual design pass
@Composable
fun CalendarTabScreen(viewModel: CalendarViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hazeState = LocalHazeController.current.mainHazeState

    when (val state = uiState) {
        is CalendarUiState.Loading -> {
            // TODO: skeleton / shimmer
        }

        is CalendarUiState.Error -> {
            // TODO: error state composable
        }

        is CalendarUiState.Success -> {
            // Local staging area for the selected day's declaration — only
            // written back to the ViewModel when "valider" is tapped.
            // Re-seeded from the freshly loaded day whenever selectedDate changes.
            var flowIntensity by remember(state.selectedDate) { mutableStateOf(state.selectedDayFlowIntensity) }
            var painLevel by remember(state.selectedDate) { mutableStateOf(state.selectedDayPainLevel) }
            var selectedSymptoms by remember(state.selectedDate) { mutableStateOf(state.selectedDaySymptoms) }
            var note by remember(state.selectedDate) { mutableStateOf(state.selectedDayNote ?: "") }

            // "valider" is only actionable once the staged declaration actually
            // differs from what's currently persisted for this day.
            val hasChanges = flowIntensity != state.selectedDayFlowIntensity ||
                    painLevel != state.selectedDayPainLevel ||
                    selectedSymptoms.map { it.id }.toSet() != state.selectedDaySymptoms.map { it.id }.toSet() ||
                    note != (state.selectedDayNote ?: "")

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                CardBase(modifier = Modifier.fillMaxWidth(), hazeState = hazeState) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        TrackingCalendarHeader(
                            monthNumber = state.monthNumber,
                            monthName = state.monthName,
                            onPreviousMonth = viewModel::onPreviousMonth,
                            onNextMonth = viewModel::onNextMonth,
                        )
                        TrackingCalendarGrid(
                            days = state.days,
                            onDayClick = viewModel::onDaySelected,
                        )
                        CalendarLegend()
                    }
                }

                SymptomDeclarationCard(
                    selectedDate = state.selectedDate,
                    flowIntensity = flowIntensity,
                    painLevel = painLevel,
                    allSymptoms = state.allSymptoms,
                    selectedSymptoms = selectedSymptoms,
                    note = note,
                    canValidate = hasChanges,
                    onFlowIntensityChange = { flowIntensity = it },
                    onPainLevelChange = { painLevel = it },
                    onSymptomToggle = { symptom ->
                        selectedSymptoms = if (selectedSymptoms.any { it.id == symptom.id }) {
                            selectedSymptoms.filterNot { it.id == symptom.id }
                        } else {
                            selectedSymptoms + symptom
                        }
                    },
                    onAddCustomSymptom = { name, category ->
                        viewModel.onAddCustomSymptom(name, category) { symptom ->
                            selectedSymptoms = selectedSymptoms + symptom
                        }
                    },
                    onNoteChange = { note = it },
                    onValidate = {
                        viewModel.onSaveDayLog(
                            flowIntensity = flowIntensity,
                            painLevel = painLevel,
                            symptoms = selectedSymptoms,
                            note = note.ifBlank { null },
                        )
                    },
                    onReset = {
                        flowIntensity = state.selectedDayFlowIntensity
                        painLevel = state.selectedDayPainLevel
                        selectedSymptoms = state.selectedDaySymptoms
                        note = state.selectedDayNote ?: ""
                    },
                )
            }
        }
    }
}
