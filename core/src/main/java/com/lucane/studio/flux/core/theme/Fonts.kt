package com.lucane.studio.flux.core.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.R

object AsAFont{
    val extraLight = FontFamily(Font(R.font.inter, weight = FontWeight.ExtraLight))
    val light = FontFamily(Font(R.font.inter, weight = FontWeight.Light))
    val regular = FontFamily(Font(R.font.inter, weight = FontWeight.Normal))
    val medium = FontFamily(Font(R.font.inter, weight = FontWeight.Medium))
    val semiBold = FontFamily(Font(R.font.inter, weight = FontWeight.SemiBold))
    val bold = FontFamily(Font(R.font.inter, weight = FontWeight.Bold))
}

val TextStyle.Companion.AsADefault: TextStyle
    get() = TextStyle(
        fontSize = 11.sp,
        fontFamily = AsAFont.regular,
        color = AsAColors.black
    )