package com.lucane.studio.flux.app.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.lucane.studio.flux.app.LocalMainViewModel
import com.lucane.studio.flux.core.navigation.EnumPages
import com.lucane.studio.flux.feature.calendar.presentation.CalendarTabScreen
import com.lucane.studio.flux.feature.details.presentation.DetailsScreen
import com.lucane.studio.flux.feature.home.presentation.HomeScreen
import com.lucane.studio.flux.feature.notes.presentation.NotesScreen

private const val TAB_TRANSITION_DURATION_MS = 250

/**
 * Shell that switches between the bottom-nav tabs (HOME, CALENDAR, DETAILS,
 * NOTES) based on [com.lucane.studio.flux.app.MainViewModel.selectedPage].
 *
 * Tabs are siblings, not a back stack — switching one never pushes/pops a
 * destination, it only swaps displayed content, so [AnimatedContent] is used
 * instead of a nested NavHost. The slide direction mirrors the tab's position
 * in the navbar (left-to-right = [EnumPages] declaration order).
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val mainViewModel = LocalMainViewModel.current
    val selectedPage by mainViewModel.selectedPage.collectAsState()

    AnimatedContent(
        // Width must be fixed (not wrap-content) for the slide distance to be
        // correct: slideInHorizontally/slideOutHorizontally compute their
        // offset from the *entering/exiting content's own measured width*, and
        // the still-empty placeholder tabs (Home/Details/Notes) have zero
        // intrinsic size — without this, entering one of them slid zero
        // pixels instead of sliding in from off-screen.
        modifier = modifier.fillMaxWidth(),
        targetState = selectedPage,
        transitionSpec = {
            val forward = targetState.ordinal > initialState.ordinal
            val enterDirection = if (forward) 1 else -1

            (slideInHorizontally(tween(TAB_TRANSITION_DURATION_MS)) { it * enterDirection } +
                fadeIn(tween(TAB_TRANSITION_DURATION_MS)))
                .togetherWith(
                    slideOutHorizontally(tween(TAB_TRANSITION_DURATION_MS)) { -it * enterDirection } +
                        fadeOut(tween(TAB_TRANSITION_DURATION_MS))
                )
                // Disable the default spring-driven size interpolation: without
                // this, switching between a tall screen (Calendar) and an empty
                // one (Home/Details/Notes) also animates height, which fights
                // the horizontal slide with an accordion-like resize.
                .using(SizeTransform(clip = false))
        },
        label = "MainScreenTabTransition",
    ) { page ->
        when (page) {
            EnumPages.HOME     -> HomeScreen()
            EnumPages.CALENDAR -> CalendarTabScreen()
            EnumPages.DETAILS  -> DetailsScreen()
            EnumPages.NOTES    -> NotesScreen()
        }
    }
}
