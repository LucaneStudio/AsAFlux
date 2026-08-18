package com.lucane.studio.flux.feature.calendar.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.ui.cards.CardBase
import com.lucane.studio.flux.core.ui.inputs.buttons.PrimaryButton
import com.lucane.studio.flux.core.providers.LocalHazeController
import com.lucane.studio.flux.feature.calendar.presentation.CalendarDayUiState
import java.time.LocalDate
import com.lucane.studio.flux.core.R as CoreRes

/**
 * Main calendar card: month grid on the left, countdown on the right,
 * and a full-width CTA button at the bottom.
 *
 * @param monthNumber      Zero-padded month number, e.g. "01".
 * @param monthName        Localised month name.
 * @param days             Flat list of 35 or 42 [com.lucane.studio.flux.feature.calendar.presentation.CalendarDayUiState] cells.
 * @param daysRemaining    Days until next predicted period. Null when unknown.
 * @param isPeriodActive   Whether a period is currently ongoing (toggles button label).
 */
@Composable
fun CalendarCard(
    modifier: Modifier = Modifier,
    monthNumber: String,
    monthName: String,
    days: List<CalendarDayUiState>,
    daysRemaining: Int?,
    isPeriodActive: Boolean,
    onPeriodStart: () -> Unit,
    onPeriodEnd: () -> Unit,
    onDayClick: (LocalDate) -> Unit = {},
) {
    val hazeState = LocalHazeController.current.mainHazeState

    CardBase(
        modifier = modifier.fillMaxWidth(),
        hazeState = hazeState,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.width(203.dp)) {
                    CalendarMonthLabel(
                        modifier = Modifier.fillMaxWidth(),
                        monthNumber = monthNumber,
                        monthName = monthName,
                    )
                    Spacer(Modifier.size(5.dp))
                    CalendarGrid(
                        days = days,
                        onDayClick = onDayClick,
                    )
                }
                Spacer(Modifier.size(5.dp))
                CalendarCountdown(
                    modifier = Modifier.weight(1f),
                    daysRemaining = daysRemaining,
                )
            }

            Spacer(Modifier.size(10.dp))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(
                    if (isPeriodActive) CoreRes.string.calendar_end_period
                    else CoreRes.string.calendar_start_period
                ),
                onClick = if (isPeriodActive) onPeriodEnd else onPeriodStart,
            )
        }
    }
}