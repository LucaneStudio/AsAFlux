package com.lucane.studio.flux.core.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.ui.utils.HeaderInfos
import com.lucane.studio.flux.core.utils.LocalHazeController
import dev.chrisbanes.haze.HazeState


@Composable
fun CardWithHeader(
    modifier: Modifier = Modifier,
    headerInfos: HeaderInfos,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {
    val hazeState = LocalHazeController.current.mainHazeState

    CardBase(
        modifier = modifier,
        hazeState = hazeState,
        contentAlignment = contentAlignment
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp))  {
            Button(
                modifier = Modifier.fillMaxWidth().height(if(headerInfos.subLabelRes != null) 31.dp else 24.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = AsAColors.blueNeon,
                    containerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(6.dp),
                enabled = headerInfos.iconRes != null,
                contentPadding = PaddingValues(horizontal = 4.dp),
                onClick = headerInfos.onClick
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier = Modifier.height(if(headerInfos.subLabelRes != null) 31.dp else 24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = headerInfos.labelRes,
                            style = TextStyle(
                                fontFamily = AsAFont.semiBold,
                                fontSize = 16.sp,
                                color = AsAColors.black,
                            ),
                        )

                        if(headerInfos.subLabelRes != null){
                            Text(
                                text = headerInfos.subLabelRes,
                                style = TextStyle(
                                    fontFamily = AsAFont.regular,
                                    fontSize = 10.sp,
                                    color = AsAColors.purpleGray,
                                ),
                            )
                        }
                    }


                    Box(Modifier.size(24.dp)){
                        if(headerInfos.iconRes != null) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(headerInfos.iconRes),
                                contentDescription = "Header button Icon",
                                tint = AsAColors.blueNeon
                            )
                        }
                    }
                }
            }

            content.invoke()
        }
    }
}