package com.lucane.studio.flux.feature.calendar.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucane.studio.flux.feature.calendar.presentation.components.CalendarCard

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
                monthNumber = state.monthNumber,
                monthName = state.monthName,
                days = state.days,
                daysRemaining = state.daysRemaining,
                isPeriodActive = state.isPeriodActive,
                onPeriodStart = viewModel::onPeriodStart,
                onPeriodEnd = viewModel::onPeriodEnd,
                onDayClick = viewModel::onDaySelected,
            )
        }
    }
}