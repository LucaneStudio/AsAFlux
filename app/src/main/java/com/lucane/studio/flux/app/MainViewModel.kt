package com.lucane.studio.flux.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucane.studio.flux.core.ui.utils.EnumPages
import com.lucane.studio.flux.data.local.datastore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsDataStore: SettingsDataStore,
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

    fun setPage(value: EnumPages){
        _selectedPage.value = value
    }
}

val LocalMainViewModel = compositionLocalOf<MainViewModel> { error("No MainViewModel provided") }
