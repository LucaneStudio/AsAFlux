package com.lucane.studio.flux.feature.onboarding.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.ui.buttons.PrimaryTextFieldButton
import com.lucane.studio.flux.core.R as CoreRes
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingDateField(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }

    val displayText = selectedDate
        ?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
        ?: stringResource(CoreRes.string.onboarding_date_placeholder)

    PrimaryTextFieldButton(
        modifier = modifier.height(36.dp),
        value = displayText,
        endItem = {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(if(selectedDate != null) CoreRes.drawable.ic_calendar_solid else CoreRes.drawable.ic_calendar_outline),
                contentDescription = null,
                tint = if(selectedDate != null) AsAColors.purpleNeon else AsAColors.purpleLightGray
            )
        },
        onClick = { showPicker = true }
    )

    if (showPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
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