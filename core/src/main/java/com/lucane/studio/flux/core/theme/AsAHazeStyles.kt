package com.lucane.studio.flux.core.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint

object AsAHazeStyles {

    fun lightBlur(blurRadius: Dp = 10.dp): HazeStyle = HazeStyle(
        backgroundColor = Color.White,
        tints = listOf(HazeTint(AsAColors.blueNeon.copy(alpha = 0.05f))),
        blurRadius = blurRadius,
    )

    fun cardBlur(blurRadius: Dp = 10.dp): HazeStyle = HazeStyle(
        backgroundColor = Color.White,
        tints = listOf(HazeTint(AsAColors.purpleNeon.copy(alpha = 0.05f))),
        blurRadius = blurRadius,
    )

    fun dropdownBlur(blurRadius: Dp = 40.dp): HazeStyle = HazeStyle(
        backgroundColor = Color.White,
        tints = listOf(HazeTint(AsAColors.purpleNeon.copy(alpha = 0.01f))),
        blurRadius = blurRadius,
    )
}