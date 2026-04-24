package com.lucane.studio.flux.feature.onboarding.presentation.components.step

import androidx.compose.foundation.layout.*
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
import com.lucane.studio.flux.core.utils.toAnnotatedString
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingProgressHeader
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingDateField
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingTitle
import java.time.LocalDate
import com.lucane.studio.flux.core.R as CoreRes

@Composable
fun FirstCycleStep(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate?,
    isSaving: Boolean,
    onDateChanged: (LocalDate) -> Unit,
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

    LaunchedEffect(selectedDate) {
        appBase.setNavbar {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                enable = selectedDate != null,
                buttonSize = ButtonSize.M,
                label = stringResource(CoreRes.string.onboarding_first_cycle_cta),
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
            title = stringResource(CoreRes.string.onboarding_first_cycle_title),
            subTitle = stringResource(CoreRes.string.onboarding_first_cycle_subtitle)
        )

        CardBase(
            hazeState = hazeState
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = stringResource(CoreRes.string.onboarding_first_cycle_date_label),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = AsAColors.purpleGray,
                    )
                )
                Spacer(Modifier.size(4.dp))

                OnboardingDateField(
                    modifier = Modifier.fillMaxWidth(),
                    selectedDate = selectedDate,
                    onDateSelected = onDateChanged,
                )
            }
        }
        CardBase(
            hazeState = hazeState
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(CoreRes.string.onboarding_first_cycle_info_defaults),
                    style = TextStyle(
                        fontFamily = AsAFont.regular,
                        fontSize = 14.sp,
                        color = AsAColors.black,
                    )
                )
                Text(
                    text = stringResource(CoreRes.string.onboarding_first_cycle_info_settings),
                    style = TextStyle(
                        fontFamily = AsAFont.regular,
                        fontSize = 14.sp,
                        color = AsAColors.black,
                    )
                )
                Text(
                    text = stringResource(CoreRes.string.onboarding_first_cycle_info_improve),
                    style = TextStyle(
                        fontFamily = AsAFont.regular,
                        fontSize = 14.sp,
                        color = AsAColors.black,
                    )
                )
            }
        }
    }
}