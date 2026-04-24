package com.lucane.studio.flux.feature.onboarding.presentation.components.step

import android.graphics.drawable.Icon
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.ui.buttons.PrimaryButton
import com.lucane.studio.flux.core.ui.cards.CardBase
import com.lucane.studio.flux.core.ui.utils.ButtonSize
import com.lucane.studio.flux.core.utils.LocalApplicationBaseController
import com.lucane.studio.flux.core.utils.LocalHazeController
import com.lucane.studio.flux.core.utils.toAnnotatedString
import com.lucane.studio.flux.feature.onboarding.presentation.components.OnboardingTitle
import com.lucane.studio.flux.core.R as CoreRes

@Composable
fun DisclaimerStep(
    modifier: Modifier = Modifier,
    onAccept: () -> Unit,
) {
    val hazeState = LocalHazeController.current.mainHazeState
    val appBase = LocalApplicationBaseController.current

    LaunchedEffect(Unit) {
        appBase.setNavbar {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                buttonSize = ButtonSize.M,
                label = stringResource(CoreRes.string.onboarding_disclaimer_accept),
                onClick = onAccept
            )
        }
        appBase.clearHeader()
    }

    LaunchedEffect(Unit) {
        val result = "This app <b>is not a medical device.</b> It does not replace professional advice.".toAnnotatedString()
        Log.d("TEST", result.text)
        result.spanStyles.forEach {
            Log.d("TEST", "「${result.text.substring(it.start, it.end)}」 → fontFamily=${it.item.fontFamily} weight=${it.item.fontWeight}")
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        OnboardingTitle(
            title = stringResource(CoreRes.string.onboarding_disclaimer_title),
            subTitle = stringResource(CoreRes.string.onboarding_disclaimer_subtitle)
        )
        Spacer(modifier = Modifier.height(10.dp))

        CardBase(
            hazeState = hazeState
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InformationRow(
                    iconRes = CoreRes.drawable.ic_health_case,
                    labelRes = CoreRes.string.onboarding_disclaimer_not_medical
                )
                InformationRow(
                    iconRes = CoreRes.drawable.ic_stat,
                    labelRes = CoreRes.string.onboarding_disclaimer_predictions
                )
                InformationRow(
                    iconRes = CoreRes.drawable.ic_lock,
                    labelRes = CoreRes.string.onboarding_disclaimer_local_only
                )
                InformationRow(
                    iconRes = CoreRes.drawable.ic_mask,
                    labelRes = CoreRes.string.onboarding_disclaimer_consult_pro
                )
            }
        }
    }
}

@Composable
private fun InformationRow(
    @DrawableRes iconRes: Int,
    @StringRes labelRes: Int,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AsAColors.blueNeon.copy(0.2f), shape = RoundedCornerShape(6.dp))
            .padding(vertical = 7.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            modifier = Modifier.size(24.dp),
            tint = AsAColors.blueNeon,
            contentDescription = "Disclaimer Icon"
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(labelRes).toAnnotatedString(),
            style = TextStyle(
                fontSize = 14.sp,
                color = AsAColors.black,
            )
        )
    }
}