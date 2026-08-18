package com.lucane.studio.flux.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucane.studio.flux.app.LocalMainViewModel
import com.lucane.studio.flux.app.presentation.screen.MainScreen
import com.lucane.studio.flux.core.navigation.EnumPages
import com.lucane.studio.flux.core.ui.MainHeaderRow
import com.lucane.studio.flux.core.ui.Navbar
import com.lucane.studio.flux.core.providers.LocalApplicationBaseController
import com.lucane.studio.flux.feature.onboarding.presentation.OnboardingScreen

@Composable
fun AsaFluxNavGraph(
    modifier: Modifier = Modifier,
    isOnboardingCompleted: Boolean,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController    = navController,
        startDestination = if (isOnboardingCompleted) Destination.Main.route
        else Destination.Onboarding.route,
        modifier         = modifier,
    ) {
        composable(Destination.Onboarding.route) {
            val appBase = LocalApplicationBaseController.current

            LaunchedEffect(Unit) {
                appBase.clearNavbar()
            }

            OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(Destination.Main.route) {
                        popUpTo(Destination.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Destination.Main.route) {
            val appBase = LocalApplicationBaseController.current
            val mainViewModel = LocalMainViewModel.current

            // Navbar reads selectedPage directly in its own composition instead
            // of closing over a value captured once by LaunchedEffect(Unit) —
            // the slot content is re-invoked by ApplicationBase on every
            // recomposition, so the read here always sees the live value.
            LaunchedEffect(Unit) {
                appBase.setNavbar {
                    val selectedPage by mainViewModel.selectedPage.collectAsState()
                    Navbar(selectedPageId = selectedPage) {
                        mainViewModel.setPage(it)
                    }
                }
            }

            // The import/export header only makes sense on Home, so it's
            // cleared (not just hidden) on every other tab — that actually
            // collapses the reserved header space in [ApplicationBase],
            // instead of leaving a blank gap.
            LaunchedEffect(Unit) {
                mainViewModel.selectedPage.collect { page ->
                    if (page == EnumPages.HOME) {
                        appBase.setHeader { MainHeaderRow() }
                    } else {
                        appBase.clearHeader()
                    }
                }
            }

            MainScreen()
        }
    }
}