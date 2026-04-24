package com.lucane.studio.flux.core.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont

fun String.toAnnotatedString(
    defaultFontFamily: FontFamily = AsAFont.regular,
    boldFontFamily: FontFamily = AsAFont.bold,
    highlightColor: Color = AsAColors.purpleNeon,
): AnnotatedString {
    val source = this
    val tags = listOf("<b>", "</b>", "<c>", "</c>")

    // Découpe la string en segments : texte brut ou balise
    val segments = mutableListOf<String>()
    var cursor = 0
    while (cursor < source.length) {
        val nextTag = tags
            .map { it to source.indexOf(it, cursor) }
            .filter { it.second != -1 }
            .minByOrNull { it.second }

        if (nextTag == null) {
            segments.add(source.substring(cursor))
            break
        }
        if (nextTag.second > cursor) {
            segments.add(source.substring(cursor, nextTag.second))
        }
        segments.add(nextTag.first)
        cursor = nextTag.second + nextTag.first.length
    }

    // Reconstruit l'AnnotatedString en trackant les styles actifs
    return buildAnnotatedString {
        var isBold = false
        var isColored = false

        fun currentStyle() = SpanStyle(
            fontFamily = if (isBold) boldFontFamily else defaultFontFamily,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = if (isColored) highlightColor else Color.Unspecified
        )

        segments.forEach { segment ->
            when (segment) {
                "<b>" -> isBold = true
                "</b>" -> isBold = false
                "<c>" -> isColored = true
                "</c>" -> isColored = false
                else -> withStyle(currentStyle()) { append(segment) }
            }
        }
    }
}