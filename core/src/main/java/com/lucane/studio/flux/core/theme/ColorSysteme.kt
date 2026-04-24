package com.lucane.studio.flux.core.theme

import androidx.compose.ui.graphics.Color

object AsAColors{
    val blueNeon = Color(0xFF3C18FF)
    val purpleNeon = Color(0xFF593AFF)
    val redNeon = Color(0xFFFF3A3D)
    val black = Color(0xFF0B0629)
    val purpleGray = Color(0xFF483F76)
    val purpleLightGray = Color(0xFF7B6FBD)
    val purpleWhite = Color(0xFFBFB6F1)
}

data class AsAButtonColors(
    val containerColor: Color = Color.Transparent,
    val labelColor: Color = AsAColors.black,
    val iconColor: Color? = null,
    val borderColor: Color = Color.Transparent,

    val disabledContainerColor: Color = Color.Transparent,
    val disabledLabelColor: Color = AsAColors.black,
    val disabledIconColor: Color = AsAColors.black,
    val disabledBorderColor: Color = Color.Transparent,
)