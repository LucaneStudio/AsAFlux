package com.lucane.studio.flux.feature.onboarding.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucane.studio.flux.core.utils.LocalApplicationBaseController
import com.lucane.studio.flux.feature.onboarding.presentation.components.step.DisclaimerStep
import com.lucane.studio.flux.feature.onboarding.presentation.components.step.FirstCycleStep
import com.lucane.studio.flux.feature.onboarding.presentation.components.step.HistorySetupStep
import com.lucane.studio.flux.feature.onboarding.presentation.components.step.ModeSelectionStep
import com.lucane.studio.flux.feature.onboarding.presentation.components.step.OnboardingDoneStep
import com.lucane.studio.flux.feature.onboarding.presentation.components.step.QuickSetupStep
import com.lucane.studio.flux.feature.onboarding.presentation.model.OnboardingStep
import com.lucane.studio.flux.feature.onboarding.presentation.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) onOnboardingComplete()
    }

    when (uiState.currentStep) {
        OnboardingStep.Disclaimer -> DisclaimerStep(
            modifier = modifier.fillMaxSize(),
            onAccept = viewModel::onDisclaimerAccepted,
        )

        OnboardingStep.ModeSelection -> ModeSelectionStep(
            modifier       = modifier.fillMaxSize(),
            onModeSelected = viewModel::onModeSelected,
            onSkip         = viewModel::onSkip,
            onBack         = viewModel::onBack,
        )

        OnboardingStep.FirstCycle -> FirstCycleStep(
            modifier      = modifier.fillMaxSize(),
            selectedDate  = uiState.firstCycleDate,
            isSaving      = uiState.isSaving,
            onDateChanged = viewModel::onFirstCycleDateChanged,
            onComplete    = viewModel::onCompleteFirstCycle,
            onBack        = viewModel::onBack,
        )

        OnboardingStep.QuickSetup -> QuickSetupStep(
            modifier                  = modifier.fillMaxSize(),
            lastPeriodDate            = uiState.lastPeriodDate,
            cycleLengthInput          = uiState.cycleLengthInput,
            bleedingDurationInput     = uiState.bleedingDurationInput,
            isSaving                  = uiState.isSaving,
            isValid                   = uiState.isQuickSetupValid,
            onLastPeriodDateChanged   = viewModel::onLastPeriodDateChanged,
            onCycleLengthChanged      = viewModel::onCycleLengthChanged,
            onBleedingDurationChanged = viewModel::onBleedingDurationChanged,
            onComplete                = viewModel::onCompleteQuickSetup,
            onBack                    = viewModel::onBack,
        )

        OnboardingStep.HistorySetup -> HistorySetupStep(
            modifier                        = modifier.fillMaxSize(),
            cycleHistory                    = uiState.cycleHistory,
            bleedingDurationInput           = uiState.bleedingDurationHistoryInput,
            filledCount                     = uiState.filledHistoryCount,
            estimatedCycleLength            = uiState.estimatedCycleLength,
            isSaving                        = uiState.isSaving,
            isValid                         = uiState.isHistorySetupValid,
            onCycleHistoryDateChanged       = viewModel::onCycleHistoryDateChanged,
            onBleedingDurationHistoryChanged = viewModel::onBleedingDurationHistoryChanged,
            onComplete                      = viewModel::onCompleteHistorySetup,
            onBack                          = viewModel::onBack,
        )

        OnboardingStep.Done -> OnboardingDoneStep(
            modifier        = modifier.fillMaxSize(),
            onNavigateToApp = viewModel::onNavigateToApp,
        )
    }
}