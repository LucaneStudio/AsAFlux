package com.lucane.studio.flux.feature.calendar.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.feature.calendar.presentation.CalendarDayUiState

// Hoisted out of composition — same rationale as CalendarDayCell.
private val CellTextRegular = TextStyle(fontFamily = AsAFont.regular, fontSize = 15.sp)
private val CellTextBold = TextStyle(fontFamily = AsAFont.bold, fontSize = 15.sp)
private val PillCorner = 8.dp
private val FlatCorner = 3.dp

/**
 * Day cell for the tracking screen ("Suivi") — visually distinct from
 * [CalendarDayCell] (Home's compact widget) per the provided mockup: period
 * days fill solid and connect into one continuous pill across a streak
 * (rounded only on the streak's exposed start/end edges, matching
 * [CalendarDayUiState.isPeriodStart]/[isPeriodEnd]), the fertile window gets
 * a soft tinted background, ovulation is an underline, and a symptom dot
 * sits in the corner. Same [CalendarDayUiState] as Home — layout only differs.
 */
// TODO DA: layout placeholder — pending the final visual design pass
@Composable
fun TrackingDayCell(
    modifier: Modifier = Modifier,
    state: CalendarDayUiState,
    onClick: () -> Unit = {},
) {
    val isFilled = state.isPeriod || state.isNextPeriod

    // Period and the next-period prediction share the same solid fill, so
    // whichever of the two applies to this day drives its pill caps.
    val isFillStart = if (state.isPeriod) state.isPeriodStart else state.isNextPeriodStart
    val isFillEnd = if (state.isPeriod) state.isPeriodEnd else state.isNextPeriodEnd

    val pillShape = RoundedCornerShape(
        topStart = if (isFillStart) PillCorner else FlatCorner,
        bottomStart = if (isFillStart) PillCorner else FlatCorner,
        topEnd = if (isFillEnd) PillCorner else FlatCorner,
        bottomEnd = if (isFillEnd) PillCorner else FlatCorner,
    )

    val fertileShape = RoundedCornerShape(
        topStart = if (state.isFertileStart) PillCorner else FlatCorner,
        bottomStart = if (state.isFertileStart) PillCorner else FlatCorner,
        topEnd = if (state.isFertileEnd) PillCorner else FlatCorner,
        bottomEnd = if (state.isFertileEnd) PillCorner else FlatCorner,
    )

    val textColor = when {
        !state.isCurrentMonth -> AsAColors.purpleWhite
        isFilled -> Color.White
        else -> AsAColors.black
    }

    Box(
        modifier = modifier
            .height(36.dp)
            .clickable(onClick = onClick)
            .then(
                if (state.isSelected && !isFilled) {
                    Modifier.border(1.dp, AsAColors.purpleNeon, RoundedCornerShape(PillCorner))
                } else {
                    Modifier
                }
            )
            .padding(2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    when {
                        isFilled -> Modifier.background(
                            color = if (state.isSelected) AsAColors.purpleGray else AsAColors.purpleNeon.copy(
                                if (!state.isCurrentMonth) 0.8f else 1f
                            ),
                            shape = pillShape
                        )

                        state.isFertile -> Modifier.background(
                            color = AsAColors.purpleWhite,
                            shape = fertileShape,
                        )

                        else -> Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.dayOfMonth.toString(),
                color = textColor,
                style = if (isFilled) CellTextBold else CellTextRegular,
            )

            if (state.isOvulation) {
                Box(
                    Modifier.fillMaxSize().padding(bottom = 3.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(3.dp)
                            .background(
                                color = if (isFilled) Color.White else AsAColors.purpleNeon,
                                shape = RoundedCornerShape(1.dp),
                            ),
                    )
                }
            }

            if (state.hasDeclaration) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(6.dp)
                        .background(color = AsAColors.lightBlueNeon, shape = CircleShape),
                )
            }
        }
    }
}
