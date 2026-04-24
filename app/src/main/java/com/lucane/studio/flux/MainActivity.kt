package com.lucane.studio.flux

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lucane.studio.flux.app.MainViewModel
import com.lucane.studio.flux.core.utils.HazeController
import com.lucane.studio.flux.core.utils.LocalHazeController
import com.lucane.studio.flux.core.ui.ApplicationBase
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.haze.rememberHazeState
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucane.studio.flux.app.LocalMainViewModel
import com.lucane.studio.flux.app.navigation.AsaFluxNavGraph
import com.lucane.studio.flux.core.utils.ApplicationBaseViewModel
import com.lucane.studio.flux.core.utils.LocalApplicationBaseController

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            val systemUiController = rememberSystemUiController()
            val hazeState = rememberHazeState()
            val mainViewModel: MainViewModel = hiltViewModel()
            val appBaseViewModel: ApplicationBaseViewModel = hiltViewModel()
            val isOnboardingCompleted by mainViewModel.isOnboardingCompleted
                .collectAsStateWithLifecycle()


            WindowCompat.setDecorFitsSystemWindows(window, false)

            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = true
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                navigationBarContrastEnforced = false,
                darkIcons = true
            )

            CompositionLocalProvider(
                LocalHazeController provides HazeController(hazeState),
                LocalMainViewModel provides mainViewModel,
                LocalApplicationBaseController provides appBaseViewModel
            ) {
                ApplicationBase {
                    when (val completed = isOnboardingCompleted) {
                        null  -> Box(Modifier.fillMaxSize())
                        else  -> AsaFluxNavGraph(isOnboardingCompleted = completed)
                    }
                }
            }
        }
    }
}