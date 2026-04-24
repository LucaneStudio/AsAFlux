package com.lucane.studio.flux.feature.onboarding.presentation.components.step

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.ui.buttons.PrimaryButton
import com.lucane.studio.flux.core.ui.cards.CardBase
import com.lucane.studio.flux.core.ui.utils.ButtonSize
import com.lucane.studio.flux.core.utils.LocalApplicationBaseController
import com.lucane.studio.flux.core.utils.LocalHazeController
import com.lucane.studio.flux.core.utils.toAnnotatedString
import com.lucane.studio.flux.feature.onboarding.presentation.components.CycleHistoryDateSlot
import com.lucane.studio.flux.feature.onboarding.presentation.components.DurationInputField
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingDateField
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingProgressHeader
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingTitle
import java.time.LocalDate
import com.lucane.studio.flux.core.R as CoreRes

@Composable
fun HistorySetupStep(
    modifier: Modifier = Modifier,
    cycleHistory: List<LocalDate?>,
    bleedingDurationInput: String,
    filledCount: Int,
    estimatedCycleLength: Int?,
    isSaving: Boolean,
    isValid: Boolean,
    onCycleHistoryDateChanged: (Int, LocalDate?) -> Unit,
    onBleedingDurationHistoryChanged: (String) -> Unit,
    onComplete: () -> Unit,
    onBack: () -> Unit,
) {
    val hazeState = LocalHazeController.current.mainHazeState
    val appBase = LocalApplicationBaseController.current


    LaunchedEffect(Unit) {
        appBase.setHeader {
            OnboardingProgressHeader(
                currentStep = 2,
                showBackButton = true,
                onBack = onBack,
            )
        }
    }

    LaunchedEffect(isValid) {
        appBase.setNavbar {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                enable = isValid,
                buttonSize = ButtonSize.M,
                label = stringResource(CoreRes.string.onboarding_cta_calculate),
                onClick = onComplete
            )
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        OnboardingTitle(
            title = stringResource(CoreRes.string.onboarding_history_title),
            subTitle = stringResource(CoreRes.string.onboarding_history_subtitle)
        )
        CardBase(
            hazeState = hazeState
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false,
                ) {
                    itemsIndexed(cycleHistory) { index, date ->
                        CycleHistoryDateSlot(
                            index = index,
                            date = date,
                            onDateSelected = { onCycleHistoryDateChanged(index, it) },
                        )
                    }
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(CoreRes.string.onboarding_history_bleeding_label),
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = AsAColors.purpleGray,
                            )
                        )
                        Text(
                            text = "1-15",
                            style = TextStyle(
                                fontFamily = AsAFont.regular,
                                fontSize = 12.sp,
                                color = AsAColors.purpleLightGray,
                            )
                        )
                    }

                    Spacer(Modifier.size(4.dp))

                    DurationInputField(
                        value = bleedingDurationInput,
                        range = 1..15,
                        onValueChange = onBleedingDurationHistoryChanged,
                    )
                }
                Text(
                    text = stringResource(
                        CoreRes.string.onboarding_history_estimated_cycle,
                        estimatedCycleLength ?: 0,
                        filledCount,
                    ).toAnnotatedString(),
                    style = TextStyle(
                        fontFamily = AsAFont.medium,
                        fontSize = 14.sp,
                        color = AsAColors.black,
                    )
                )
            }
        }
    }
}