package com.lucane.studio.flux.core.ui.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class HeaderInfos(
    @DrawableRes val iconRes: Int? = null,
    val labelRes: String,
    val subLabelRes: String? = null,
    val onClick: () -> Unit
)