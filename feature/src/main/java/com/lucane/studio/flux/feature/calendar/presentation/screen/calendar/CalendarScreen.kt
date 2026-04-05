package com.lucane.studio.flux.feature.calendar.presentation.screen.calendar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucane.studio.flux.feature.calendar.presentation.components.calendar.CalendarCard
import com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state.CalendarUiState
import com.lucane.studio.flux.feature.calendar.presentation.screen.calendar.CalendarViewModel
import dev.chrisbanes.haze.HazeState

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
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
                hazeState = hazeState,
                monthNumber = state.monthNumber,
                monthName = state.monthName,
                days = state.days,
                daysRemaining = state.daysRemaining,
                isPeriodActive = state.isPeriodActive,
            )
        }
    }
}