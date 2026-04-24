package com.lucane.studio.flux.feature.onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.ui.buttons.PrimaryTextFieldButton
import com.lucane.studio.flux.core.R as CoreRes
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CycleHistoryDateSlot(
    modifier: Modifier = Modifier,
    index: Int,
    date: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }

    val label = date
        ?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
        ?: stringResource(CoreRes.string.action_tap_to_enter)

    PrimaryTextFieldButton(
        modifier = modifier.height(36.dp),
        value = label,
        borderColor = if(date != null) AsAColors.purpleNeon else AsAColors.purpleNeon.copy(0.2f),
        endItem = {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(if(date != null) CoreRes.drawable.ic_calendar_solid else CoreRes.drawable.ic_calendar_outline),
                contentDescription = null,
                tint = if(date != null) AsAColors.purpleNeon else AsAColors.purpleLightGray
            )
        },
        startItem = {
            when{
                date != null -> Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(
                            color = AsAColors.purpleNeon,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "${index + 1}",
                        style = TextStyle(
                            fontFamily = AsAFont.bold,
                            fontSize = 14.sp,
                            color = Color.White,
                        )
                    )
                }

                else -> Box(
                    modifier = Modifier
                        .size(18.dp)
                        .border(
                            width = 1.dp,
                            color = AsAColors.purpleWhite,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "${index + 1}",
                        style = TextStyle(
                            fontFamily = AsAFont.bold,
                            fontSize = 14.sp,
                            color = AsAColors.purpleGray,
                        )
                    )
                }
            }

        },
        onClick = { showPicker = true }
    )

    if (showPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date
                ?.atStartOfDay(ZoneOffset.UTC)
                ?.toInstant()
                ?.toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton    = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis
                        ?.let { Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate() }
                        ?.let(onDateSelected)
                    showPicker = false
                }) { Text(stringResource(CoreRes.string.common_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text(stringResource(CoreRes.string.common_cancel))
                }
            },
        ) { DatePicker(state = datePickerState) }
    }
}