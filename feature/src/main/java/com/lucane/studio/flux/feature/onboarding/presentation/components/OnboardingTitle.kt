package com.lucane.studio.flux.feature.onboarding.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.R
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont

@Composable
fun OnboardingTitle(
    title: String,
    subTitle: String
){
    Column(modifier = Modifier.fillMaxWidth().padding(start = 6.dp)) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = AsAFont.bold,
                fontSize = 20.sp,
                color = AsAColors.black,
            )
        )
        Text(
            text = subTitle,
            style = TextStyle(
                fontFamily = AsAFont.regular,
                fontSize = 14.sp,
                color = AsAColors.black,
            )
        )
    }
}