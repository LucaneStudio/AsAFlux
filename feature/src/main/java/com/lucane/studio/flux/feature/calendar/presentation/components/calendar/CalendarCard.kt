package com.lucane.studio.flux.feature.calendar.presentation.components.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.ui.cards.CardBase
import com.lucane.studio.flux.core.ui.buttons.DropdownToggleAction
import com.lucane.studio.flux.core.ui.buttons.DropdownToggleMenu
import com.lucane.studio.flux.core.ui.buttons.IconLightButton
import com.lucane.studio.flux.core.ui.buttons.PrimaryButton
import com.lucane.studio.flux.core.utils.LocalHazeController
import com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state.CalendarDayUiState
import com.lucane.studio.flux.feature.calendar.presentation.screen.calendar.CalendarViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import com.lucane.studio.flux.core.R as CoreRes

/**
 * Main calendar card: month grid on the left, countdown on the right,
 * and a full-width CTA button at the bottom.
 *
 * @param monthNumber      Zero-padded month number, e.g. "01".
 * @param monthName        Localised month name.
 * @param days             Flat list of 35 or 42 [com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state.CalendarDayUiState] cells.
 * @param daysRemaining    Days until next predicted period. Null when unknown.
 * @param isPeriodActive   Whether a period is currently ongoing (toggles button label).
 */
@Composable
fun CalendarCard(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
    monthNumber: String,
    monthName: String,
    days: List<CalendarDayUiState>,
    daysRemaining: Int?,
    isPeriodActive: Boolean,
) {
    val showFertility by viewModel.showFertility.collectAsState()
    val showOvulation by viewModel.showOvulation.collectAsState()
    val hazeState = LocalHazeController.current.mainHazeState
    var menuExpanded by remember { mutableStateOf(false) }

    val internHazeState = rememberHazeState()

    CardBase(
        modifier = modifier.fillMaxWidth().hazeSource(internHazeState),
        hazeState = hazeState,
        modularButton = listOf(
            {
                Box {
                    IconLightButton(
                        icon = painterResource(CoreRes.drawable.ic_cog),
                    ) {
                        menuExpanded = true
                    }

                    DropdownToggleMenu(
                        hazeState = internHazeState,
                        expanded = menuExpanded,
                        onDismiss = { menuExpanded = false },
                        actions = listOf(
                            DropdownToggleAction(
                                icon = painterResource(CoreRes.drawable.ic_ovum),
                                label = stringResource(CoreRes.string.calendar_toggle_ovulation),
                                isActive = showOvulation,
                                onToggle = viewModel::onToggleOvulation,
                            ),
                            DropdownToggleAction(
                                icon = painterResource(CoreRes.drawable.ic_fertility),
                                label = stringResource(CoreRes.string.calendar_toggle_fertile),
                                isActive = showFertility,
                                onToggle = viewModel::onToggleFertileWindow,
                            ),
                        ),
                    )
                }
            }
        )
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
                onClick = {},
            )
        }
    }
}