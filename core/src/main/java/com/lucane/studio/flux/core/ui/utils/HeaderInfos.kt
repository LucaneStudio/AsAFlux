package com.lucane.studio.flux.core.ui.utils

import androidx.annotation.DrawableRes
import androidx.compose.ui.Alignment

data class HeaderInfos(
    @DrawableRes val endIconRes: Int? = null,
    @DrawableRes val startIconRes: Int? = null,
    val endIconAlignment: Alignment = Alignment.TopStart,
    val labelRes: String,
    val subLabelRes: String? = null,
    val onClick: () -> Unit
)