package com.lucane.studio.flux.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ApplicationBaseViewModel: ViewModel() {

    private val _header = MutableStateFlow<(@Composable () -> Unit)?>(null)
    val header: StateFlow<(@Composable () -> Unit)?> = _header.asStateFlow()

    private val _navbar = MutableStateFlow<(@Composable () -> Unit)?>(null)
    val navbar: StateFlow<(@Composable () -> Unit)?> = _navbar.asStateFlow()

    fun setHeader(newHeader: @Composable () -> Unit){
        _header.value = newHeader
    }

    fun clearHeader(){
        _header.value = null
    }

    fun setNavbar(newHeader: @Composable () -> Unit){
        _navbar.value = newHeader
    }

    fun clearNavbar(){
        _navbar.value = null
    }
}


val LocalApplicationBaseController = compositionLocalOf<ApplicationBaseViewModel> { error("No ApplicationBaseViewModel provided") }