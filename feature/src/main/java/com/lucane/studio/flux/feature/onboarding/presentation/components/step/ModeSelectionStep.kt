package com.lucane.studio.flux.feature.onboarding.presentation.components.step

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.ui.cards.CardWithHeader
import com.lucane.studio.flux.core.ui.utils.HeaderInfos
import com.lucane.studio.flux.core.utils.LocalApplicationBaseController
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingProgressHeader
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingModeCard
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingTitle
import com.lucane.studio.flux.feature.onboarding.presentation.model.OnboardingMode
import com.lucane.studio.flux.core.R as CoreRes

@Composable
fun ModeSelectionStep(
    modifier: Modifier = Modifier,
    onModeSelected: (OnboardingMode) -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
) {
    val appBase = LocalApplicationBaseController.current

    LaunchedEffect(Unit) {
        appBase.setHeader {
            OnboardingProgressHeader(
                currentStep = 1,
                showBackButton = true,
                onBack = onBack,
            )
        }

        appBase.setNavbar { Box(Modifier.size(40.dp)) }
    }


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        OnboardingTitle(
            title = stringResource(CoreRes.string.onboarding_mode_title),
            subTitle = stringResource(CoreRes.string.onboarding_mode_subtitle)
        )

        CardWithHeader(
            headerInfos = HeaderInfos(
                endIconRes = CoreRes.drawable.ic_chevron_end,
                startIconRes = CoreRes.drawable.ic_bolt,
                labelRes = stringResource(CoreRes.string.onboarding_mode_quick_title),
                subLabelRes = stringResource(CoreRes.string.onboarding_mode_quick_desc),
                endIconAlignment = Alignment.Center,
                onClick = { onModeSelected(OnboardingMode.QUICK) }
            )
        ) { }

        CardWithHeader(
            headerInfos = HeaderInfos(
                endIconRes = CoreRes.drawable.ic_chevron_end,
                startIconRes = CoreRes.drawable.ic_calendar_solid,
                labelRes = stringResource(CoreRes.string.onboarding_mode_history_title),
                subLabelRes = stringResource(CoreRes.string.onboarding_mode_history_desc),
                endIconAlignment = Alignment.Center,
                onClick = { onModeSelected(OnboardingMode.HISTORY) }
            )
        ) { }

        CardWithHeader(
            headerInfos = HeaderInfos(
                endIconRes = CoreRes.drawable.ic_chevron_end,
                startIconRes = CoreRes.drawable.ic_pin,
                labelRes = stringResource(CoreRes.string.onboarding_mode_first_cycle_title),
                subLabelRes = stringResource(CoreRes.string.onboarding_mode_first_cycle_desc),
                endIconAlignment = Alignment.Center,
                onClick = { onModeSelected(OnboardingMode.FIRST_CYCLE) }
            )
        ) { }
    }
}