package com.lucane.studio.flux.feature.onboarding.domain.usecase

import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
) {
    suspend operator fun invoke() = settingsDataStore.setOnboardingCompleted(true)
}