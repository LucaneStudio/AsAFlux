package com.lucane.studio.flux.feature.onboarding.presentation.components.step

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.ui.buttons.PrimaryButton
import com.lucane.studio.flux.core.ui.utils.ButtonSize
import com.lucane.studio.flux.core.utils.LocalApplicationBaseController
import com.lucane.studio.flux.core.utils.LocalHazeController
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingProgressHeader
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingTitle
import com.lucane.studio.flux.core.R as CoreRes

@Composable
fun OnboardingDoneStep(
    modifier: Modifier = Modifier,
    onNavigateToApp: () -> Unit,
) {
    val appBase = LocalApplicationBaseController.current


    LaunchedEffect(Unit) {
        appBase.setHeader {
            OnboardingProgressHeader(
                currentStep = 3,
                showBackButton = false,
                onBack = {}
            )
        }

        appBase.setNavbar {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                buttonSize = ButtonSize.M,
                label = stringResource(CoreRes.string.onboarding_done_cta),
                onClick = onNavigateToApp
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
            title = stringResource(CoreRes.string.onboarding_done_title),
            subTitle = stringResource(CoreRes.string.onboarding_done_subtitle)
        )
    }
}