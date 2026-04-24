package com.lucane.studio.flux.feature.onboarding.presentation.components.step

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.lucane.studio.flux.core.R as CoreRes
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingDateField
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingProgressHeader
import com.lucane.studio.flux.feature.onboarding.presentation.components.DurationInputField
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingTitle
import java.time.LocalDate

@Composable
fun QuickSetupStep(
    modifier: Modifier = Modifier,
    lastPeriodDate: LocalDate?,
    cycleLengthInput: String,
    bleedingDurationInput: String,
    isSaving: Boolean,
    isValid: Boolean,
    onLastPeriodDateChanged: (LocalDate) -> Unit,
    onCycleLengthChanged: (String) -> Unit,
    onBleedingDurationChanged: (String) -> Unit,
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
            title = stringResource(CoreRes.string.onboarding_quick_title),
            subTitle = stringResource(CoreRes.string.onboarding_quick_subtitle)
        )

        CardBase(
            hazeState = hazeState
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column {
                    Text(
                        modifier = Modifier.padding(start = 6.dp),
                        text = stringResource(CoreRes.string.onboarding_quick_date_label),
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = AsAColors.purpleGray,
                        )
                    )
                    Spacer(Modifier.size(4.dp))

                    OnboardingDateField(
                        selectedDate = lastPeriodDate,
                        onDateSelected = onLastPeriodDateChanged,
                    )
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = stringResource(CoreRes.string.onboarding_quick_cycle_length_label),
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = AsAColors.purpleGray,
                            )
                        )
                        Text(
                            text = "15-60",
                            style = TextStyle(
                                fontFamily = AsAFont.regular,
                                fontSize = 12.sp,
                                color = AsAColors.purpleLightGray,
                            )
                        )
                    }

                    Spacer(Modifier.size(4.dp))

                    DurationInputField(
                        value = cycleLengthInput,
                        range = 15..60,
                        onValueChange = onCycleLengthChanged,
                    )
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(CoreRes.string.onboarding_quick_bleeding_label),
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
                        onValueChange = onBleedingDurationChanged,
                    )
                }
            }

        }
    }
}