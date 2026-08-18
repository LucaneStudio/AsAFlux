package com.lucane.studio.flux.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucane.studio.flux.core.navigation.EnumPages
import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import com.lucane.studio.flux.feature.calendar.domain.usecase.AutoConfirmMissedPeriodUseCase
import com.lucane.studio.flux.feature.calendar.domain.usecase.SeedDefaultSymptomsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsDataStore: SettingsDataStore,
    autoConfirmMissedPeriod: AutoConfirmMissedPeriodUseCase,
    seedDefaultSymptoms: SeedDefaultSymptomsUseCase,
) : ViewModel() {

    private val _selectedPage = MutableStateFlow(EnumPages.HOME)
    val selectedPage: StateFlow<EnumPages> = _selectedPage.asStateFlow()

    // null = lecture en cours, Boolean = valeur connue
    val isOnboardingCompleted: StateFlow<Boolean?> = settingsDataStore.isOnboardingCompleted
        .map { it }
        .stateIn(
            scope            = viewModelScope,
            started          = SharingStarted.WhileSubscribed(5_000),
            initialValue     = null,
        )

    init {
        // Runs once per app session — catches up any period the user never
        // declared, so the cycle chain and predictions don't silently stall.
        viewModelScope.launch {
            autoConfirmMissedPeriod()
        }
        // Runs once ever (no-ops once the symptom catalog is non-empty) —
        // gives the tracking screen a starter list instead of a blank one.
        viewModelScope.launch {
            seedDefaultSymptoms()
        }
    }

    fun setPage(value: EnumPages){
        _selectedPage.value = value
    }
}

val LocalMainViewModel = compositionLocalOf<MainViewModel> { error("No MainViewModel provided") }
