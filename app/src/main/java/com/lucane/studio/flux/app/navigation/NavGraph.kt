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
import com.lucane.studio.flux.core.ui.MainHeaderRow
import com.lucane.studio.flux.core.ui.Navbar
import com.lucane.studio.flux.core.utils.LocalApplicationBaseController
import com.lucane.studio.flux.feature.calendar.presentation.screen.calendar.CalendarScreen
import com.lucane.studio.flux.feature.onboarding.presentation.screen.OnboardingScreen

@Composable
fun AsaFluxNavGraph(
    modifier: Modifier = Modifier,
    isOnboardingCompleted: Boolean,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController    = navController,
        startDestination = if (isOnboardingCompleted) Destination.Calendar.route
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
                    navController.navigate(Destination.Calendar.route) {
                        popUpTo(Destination.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Destination.Calendar.route) {
            val appBase = LocalApplicationBaseController.current
            val mainViewModel = LocalMainViewModel.current

            val selectedPage by mainViewModel.selectedPage.collectAsState()

            LaunchedEffect(Unit) {
                appBase.setHeader {
                    MainHeaderRow()
                }
                appBase.setNavbar {
                    Navbar(
                        selectedPageId = selectedPage
                    ) {
                        mainViewModel.setPage( it)
                    }
                }
            }

            CalendarScreen()
        }
    }
}