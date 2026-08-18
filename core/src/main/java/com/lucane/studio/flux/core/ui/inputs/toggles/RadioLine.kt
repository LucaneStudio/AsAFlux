package com.lucane.studio.flux.core.ui.inputs.toggles

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont

data class RadioLineItemData(
    val id: String,
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int,
    val isSelected: Boolean = false
)

@Composable
fun RadioLine(
    modifier: Modifier = Modifier,
    items: List<RadioLineItemData>,
    onValueChange: (currentId: String) -> Unit
) {
    val selectedIndex = items.indexOfFirst { it.isSelected }.coerceAtLeast(0)

    // Animates across the index range (not just a color) so the thumb
    // physically slides across every button in between, like a slider.
    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = tween(300),
        label = "selected index",
    )

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AsAColors.purpleNeon.copy(0.2f))
            .padding(3.dp)
    ) {
        val itemWidth = (maxWidth.value / items.size).dp

        Box(
            modifier = Modifier
                .offset(x = (itemWidth.value * animatedIndex).dp)
                .width(itemWidth)
                .fillMaxHeight()
                .background(AsAColors.purpleNeon, shape = RoundedCornerShape(6.dp))
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            items.forEach { item ->
                val isActive = item.isSelected

                val iconColor by animateColorAsState(
                    targetValue = if (isActive) Color.White else AsAColors.purpleNeon,
                    animationSpec = tween(300),
                    label = "icon color",
                )
                val labelColor by animateColorAsState(
                    targetValue = if (isActive) Color.White else AsAColors.purpleGray,
                    animationSpec = tween(300),
                    label = "label color",
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable { onValueChange.invoke(item.id) },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(item.iconRes),
                        contentDescription = "InternalCard Icon",
                        tint = iconColor
                    )

                    Text(
                        text = stringResource(item.labelRes),
                        style = TextStyle(
                            fontFamily = AsAFont.regular,
                            fontSize = 13.sp,
                            color = labelColor,
                        ),
                    )
                }
            }
        }
    }
}
