package com.lucane.studio.flux.feature.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucane.studio.flux.feature.onboarding.domain.model.InitialCycleData
import com.lucane.studio.flux.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.lucane.studio.flux.feature.onboarding.domain.usecase.SaveFirstCycleUseCase
import com.lucane.studio.flux.feature.onboarding.domain.usecase.SaveHistoricalCyclesUseCase
import com.lucane.studio.flux.feature.onboarding.domain.usecase.SaveQuickSetupUseCase
import com.lucane.studio.flux.feature.onboarding.presentation.model.OnboardingMode
import com.lucane.studio.flux.feature.onboarding.presentation.model.OnboardingStep
import com.lucane.studio.flux.feature.onboarding.presentation.model.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboarding: CompleteOnboardingUseCase,
    private val saveFirstCycle: SaveFirstCycleUseCase,
    private val saveQuickSetup: SaveQuickSetupUseCase,
    private val saveHistoricalCycles: SaveHistoricalCyclesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    // ── Navigation ──────────────────────────────────────────────

    fun onDisclaimerAccepted() =
        _uiState.update { it.copy(currentStep = OnboardingStep.ModeSelection) }

    fun onModeSelected(mode: OnboardingMode) {
        _uiState.update {
            it.copy(
                selectedMode = mode,
                currentStep  = when (mode) {
                    OnboardingMode.FIRST_CYCLE -> OnboardingStep.FirstCycle
                    OnboardingMode.QUICK       -> OnboardingStep.QuickSetup
                    OnboardingMode.HISTORY     -> OnboardingStep.HistorySetup
                },
            )
        }
    }

    fun onBack() {
        _uiState.update {
            it.copy(
                currentStep = when (it.currentStep) {
                    OnboardingStep.ModeSelection -> OnboardingStep.Disclaimer
                    OnboardingStep.FirstCycle,
                    OnboardingStep.QuickSetup,
                    OnboardingStep.HistorySetup  -> OnboardingStep.ModeSelection
                    else                         -> return
                },
            )
        }
    }

    fun onSkip() {
        viewModelScope.launch {
            completeOnboarding()
            _uiState.update { it.copy(isComplete = true) }
        }
    }

    // ── Saisies First Cycle ──────────────────────────────────────

    fun onFirstCycleDateChanged(date: LocalDate) =
        _uiState.update { it.copy(firstCycleDate = date) }

    // ── Saisies Quick Setup ──────────────────────────────────────

    fun onLastPeriodDateChanged(date: LocalDate) =
        _uiState.update { it.copy(lastPeriodDate = date) }

    fun onCycleLengthChanged(value: String) =
        _uiState.update { it.copy(cycleLengthInput = value) }

    fun onBleedingDurationChanged(value: String) =
        _uiState.update { it.copy(bleedingDurationInput = value) }

    // ── Saisies History ──────────────────────────────────────────

    fun onCycleHistoryDateChanged(index: Int, date: LocalDate?) {
        val updated = _uiState.value.cycleHistory.toMutableList().also { it[index] = date }
        _uiState.update { it.copy(cycleHistory = updated) }
    }

    fun onBleedingDurationHistoryChanged(value: String) =
        _uiState.update { it.copy(bleedingDurationHistoryInput = value) }

    // ── Validations & sauvegarde ─────────────────────────────────

    fun onCompleteFirstCycle() {
        val date = _uiState.value.firstCycleDate ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            saveFirstCycle(date)
            completeOnboarding()
            _uiState.update { it.copy(isSaving = false, currentStep = OnboardingStep.Done) }
        }
    }

    fun onCompleteQuickSetup() {
        val state = _uiState.value
        if (!state.isQuickSetupValid) return
        val date         = state.lastPeriodDate!!
        val cycleLength  = state.cycleLengthInput.toInt()
        val bleedingDays = state.bleedingDurationInput.toInt()
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            saveQuickSetup(InitialCycleData(date, cycleLength, bleedingDays))
            completeOnboarding()
            _uiState.update { it.copy(isSaving = false, currentStep = OnboardingStep.Done) }
        }
    }

    fun onCompleteHistorySetup() {
        val state = _uiState.value
        if (!state.isHistorySetupValid) return
        val validDates   = state.cycleHistory.filterNotNull()
        val bleedingDays = state.bleedingDurationHistoryInput.toInt()
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            saveHistoricalCycles(validDates, bleedingDays)
            completeOnboarding()
            _uiState.update { it.copy(isSaving = false, currentStep = OnboardingStep.Done) }
        }
    }

    fun onNavigateToApp() =
        _uiState.update { it.copy(isComplete = true) }
}