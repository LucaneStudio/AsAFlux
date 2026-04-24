package com.lucane.studio.flux.feature.onboarding.presentation.model

sealed class OnboardingStep {
    data object Disclaimer    : OnboardingStep()
    data object ModeSelection : OnboardingStep()
    data object FirstCycle    : OnboardingStep()
    data object QuickSetup    : OnboardingStep()
    data object HistorySetup  : OnboardingStep()
    data object Done          : OnboardingStep()
}