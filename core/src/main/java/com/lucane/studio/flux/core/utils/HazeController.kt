package com.lucane.studio.flux.core.utils

import androidx.compose.runtime.compositionLocalOf
import dev.chrisbanes.haze.HazeState

class HazeController(defaultHazeState: HazeState) {
    val mainHazeState: HazeState = defaultHazeState
}

val LocalHazeController = compositionLocalOf<HazeController> { error("No HazeController provided") }