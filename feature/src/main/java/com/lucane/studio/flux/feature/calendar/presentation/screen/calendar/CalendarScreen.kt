package com.lucane.studio.flux.feature.calendar.presentation.screen.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucane.studio.flux.feature.calendar.presentation.components.calendar.CalendarCard
import com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state.CalendarUiState

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is CalendarUiState.Loading -> {
            // TODO: skeleton / shimmer
        }

        is CalendarUiState.Error -> {
            // TODO: error state composable
        }

        is CalendarUiState.Success -> {
            CalendarCard(
                viewModel = viewModel,
                monthNumber = state.monthNumber,
                monthName = state.monthName,
                days = state.days,
                daysRemaining = state.daysRemaining,
                isPeriodActive = state.isPeriodActive,
            )
        }
    }
}