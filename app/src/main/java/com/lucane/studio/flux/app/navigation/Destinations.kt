package com.lucane.studio.flux.app.navigation

sealed class Destination(val route: String) {
    data object Onboarding : Destination("onboarding")
    data object Calendar   : Destination("calendar")
}