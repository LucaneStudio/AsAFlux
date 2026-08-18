package com.lucane.studio.flux.core.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.Alignment

data class HeaderInfos(
    @DrawableRes val endIconRes: Int? = null,
    @DrawableRes val startIconRes: Int? = null,
    val endIconAlignment: Alignment = Alignment.TopStart,
    @StringRes val labelRes: Int,
    val subLabel: String? = null,
    val onClick: () -> Unit
)