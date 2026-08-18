package com.lucane.studio.flux.core.ui.cards

import android.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont

enum class InternalCardStyle{
    TOP,
    BOTTOM
}

@Composable
fun InternalCard(
    modifier: Modifier = Modifier,
    title: String,
    @StringRes subTitleRes: Int,
    @DrawableRes iconRes: Int,
    style: InternalCardStyle = InternalCardStyle.TOP,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(AsAColors.blueNeon.copy(0.2f))
            .padding(horizontal = 7.dp, vertical = 9.dp),

        ) {
        when (style) {
            InternalCardStyle.TOP -> {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = AsAFont.medium,
                        fontSize = 16.sp,
                        color = AsAColors.black,
                    ),
                )
                Text(
                    text = stringResource(subTitleRes),
                    style = TextStyle(
                        fontFamily = AsAFont.regular,
                        fontSize = 14.sp,
                        color = AsAColors.purpleGray,
                    ),
                )

                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ){
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(iconRes),
                        contentDescription = "InternalCard Icon",
                        tint = AsAColors.blueNeon
                    )
                }
            }
            InternalCardStyle.BOTTOM -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ){
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(iconRes),
                        contentDescription = "InternalCard Icon",
                        tint = AsAColors.blueNeon
                    )
                }
                Spacer(Modifier.weight(1f))

                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = AsAFont.semiBold,
                        fontSize = 18.sp,
                        color = AsAColors.black,
                    ),
                )
                Text(
                    text = stringResource(subTitleRes),
                    style = TextStyle(
                        fontFamily = AsAFont.regular,
                        fontSize = 14.sp,
                        color = AsAColors.purpleGray,
                    ),
                )
            }
        }
    }
}