package com.lucane.studio.flux.core.ui.inputs.toggles

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.R
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont

@Composable
fun CheckmarkToggle(
    modifier: Modifier = Modifier,
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)
    val checkboxShape = RoundedCornerShape(4.dp)

    val backgroundColor by animateColorAsState(
        targetValue = if (isChecked) AsAColors.purpleNeon.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "checkmark toggle background",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isChecked) AsAColors.purpleNeon else AsAColors.purpleWhite,
        animationSpec = tween(200),
        label = "checkmark toggle border",
    )
    val labelColor by animateColorAsState(
        targetValue = if (isChecked) AsAColors.purpleNeon else AsAColors.purpleGray,
        animationSpec = tween(200),
        label = "checkmark toggle label",
    )

    Row(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, borderColor, shape)
            .clickable { onCheckedChange.invoke(!isChecked) }
            .padding(horizontal = 7.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(13.dp)
                .clip(checkboxShape)
                .background(if (isChecked) AsAColors.purpleNeon else Color.White)
                .border(
                    width = 1.dp,
                    color = if (isChecked) AsAColors.purpleNeon else AsAColors.purpleWhite,
                    shape = checkboxShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (isChecked) {
                Icon(
                    modifier = Modifier.fillMaxSize(0.8f),
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }

        Text(
            text = label,
            style = TextStyle(
                fontFamily = AsAFont.semiBold,
                fontSize = 13.sp,
                color = labelColor,
            ),
        )
    }
}
