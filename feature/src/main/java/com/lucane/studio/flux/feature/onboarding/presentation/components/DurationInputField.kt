package com.lucane.studio.flux.feature.onboarding.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.ui.textfield.PrimaryTextField
import com.lucane.studio.flux.core.R as CoreRes

@Composable
fun DurationInputField(
    modifier: Modifier = Modifier,
    value: String,
    range: IntRange,
    onValueChange: (String) -> Unit,
) {
    val isError = value.toIntOrNull()?.let { it !in range } ?: true

    PrimaryTextField(
        modifier = modifier.height(36.dp),
        value = value,
        onValueChange = { if (it.length < 3) onValueChange(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        endItem = {
            Text(
                text = stringResource(CoreRes.string.common_days),
                style = TextStyle(
                    fontFamily = AsAFont.regular,
                    fontSize = 12.sp,
                    color = if (isError) AsAColors.redNeon else AsAColors.purpleLightGray,
                )
            )
        },
        isError = isError,
        errorLabel = stringResource(CoreRes.string.onboarding_duration_range_hint, range.first, range.last)
    )
}