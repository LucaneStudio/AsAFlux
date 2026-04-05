package com.lucane.studio.flux.feature.calendar.presentation.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.R as CoreRes
import com.lucane.studio.flux.feature.calendar.presentation.components.calendar.state.CalendarDayUiState

@Composable
fun CalendarDayCell(
    modifier: Modifier = Modifier,
    state: CalendarDayUiState,
) {
    val textColor = when {
        !state.isCurrentMonth -> AsAColors.purpleWhite
        state.isPeriod -> AsAColors.purpleNeon
        state.isNextPeriod -> AsAColors.purpleNeon
        else -> AsAColors.black
    }

    Box(
        modifier = modifier
            .size(29.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (state.isSelected) {
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .background(
                        color = AsAColors.blueNeon.copy(0.2f),
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }

        Box(modifier = Modifier.size(30.dp), contentAlignment = Alignment.TopEnd) {
            when {
                state.isOvulation -> Icon(
                    modifier = Modifier.size(10.dp),
                    painter = painterResource(CoreRes.drawable.ic_ovum),
                    contentDescription = null,
                    tint = if(!state.isCurrentMonth) AsAColors.purpleWhite else AsAColors.blueNeon
                )

                state.isFertile -> Icon(
                    modifier = Modifier.size(10.dp),
                    painter = painterResource(CoreRes.drawable.ic_fertility),
                    contentDescription = null,
                    tint = if(!state.isCurrentMonth) AsAColors.purpleWhite else AsAColors.blueNeon
                )

                state.isNextPeriod || state.isPeriod -> Icon(
                    modifier = Modifier.size(10.dp),
                    painter = painterResource(CoreRes.drawable.ic_period),
                    contentDescription = null,
                    tint = if(!state.isCurrentMonth) AsAColors.purpleWhite else AsAColors.blueNeon
                )

                else -> {}
            }
        }

        Text(
            // Leave room for the dot: shift content slightly upward when dot is shown
            text = state.dayOfMonth.toString(),
            style = TextStyle(
                fontFamily = if (state.isPeriod || state.isNextPeriod) AsAFont.bold else AsAFont.regular,
                fontSize = 16.sp,
                color = textColor,
            ),
        )
    }
}