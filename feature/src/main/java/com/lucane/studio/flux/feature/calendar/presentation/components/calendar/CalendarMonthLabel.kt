package com.lucane.studio.flux.feature.calendar.presentation.components.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont

/**
 * Displays the current month label as "01 janvier".
 * The month number is muted, the month name is bold.
 *
 * @param monthNumber  Zero-padded month number string, e.g. "01".
 * @param monthName    Localised month name, e.g. "janvier" / "january".
 */
@Composable
fun CalendarMonthLabel(
    modifier: Modifier = Modifier,
    monthNumber: String,
    monthName: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$monthNumber ",
            style = TextStyle(
                fontFamily = AsAFont.light,
                fontSize = 16.sp,
                color = AsAColors.purpleLightGray,
            ),
        )
        Text(
            text = monthName,
            style = TextStyle(
                fontFamily = AsAFont.regular,
                fontSize = 16.sp,
                color = AsAColors.black,
            ),
        )
    }
}