package com.lucane.studio.flux.feature.onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.ui.buttons.IconLightBlurButton
import com.lucane.studio.flux.core.ui.buttons.LightBlurButton
import com.lucane.studio.flux.core.utils.LocalHazeController
import com.lucane.studio.flux.core.R as CoreRes


@Composable
fun OnboardingProgressHeader(
    modifier: Modifier = Modifier,
    currentStep: Int,
    showBackButton: Boolean,
    onBack: () -> Unit,
) {
    val hazeState = LocalHazeController.current.mainHazeState

    Row(
        modifier.padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showBackButton) {
            IconLightBlurButton(
                icon = painterResource(CoreRes.drawable.ic_arrow_start),
                hazeState = hazeState,
                onClick = onBack
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(
                        color = AsAColors.blueNeon,
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(
                        color = if(currentStep > 1) AsAColors.blueNeon else AsAColors.blueNeon.copy(0.3f),
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(
                        color = if(currentStep > 2) AsAColors.blueNeon else AsAColors.blueNeon.copy(0.3f),
                        shape = CircleShape
                    )
            )
        }

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text= currentStep.toString(),
                style = TextStyle(
                    fontFamily = AsAFont.semiBold,
                    fontSize = 16.sp,
                    color = AsAColors.blueNeon,
                )
            )
            Spacer(Modifier.size(2.dp))
            Text(
                modifier = Modifier.padding(bottom = 1.dp),
                text = "/3",
                style = TextStyle(
                    fontFamily = AsAFont.regular,
                    fontSize = 10.sp,
                    color = AsAColors.black,
                )
            )
        }
    }
}