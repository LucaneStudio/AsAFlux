package com.lucane.studio.flux.core.ui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.theme.AsAButtonColors
import com.lucane.studio.flux.core.theme.AsADefault
import com.lucane.studio.flux.core.ui.utils.IconGravity

@Composable
fun ButtonBase(
    modifier: Modifier = Modifier,
    colors: AsAButtonColors,
    label: String? = null,
    textStyle: TextStyle = TextStyle.AsADefault,
    icon: Painter? = null,
    iconGravity: IconGravity = IconGravity.START,
    enable: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    paddingValues: PaddingValues,
    spacing: Dp = 8.dp,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            contentColor = colors.labelColor,
            containerColor = colors.containerColor,
            disabledContentColor = colors.disabledLabelColor,
            disabledContainerColor = colors.disabledContainerColor
        ),
        shape = shape,
        border = BorderStroke(
            width = 1.dp,
            color = if (enable) colors.borderColor else colors.disabledBorderColor
        ),
        contentPadding = paddingValues,
        enabled = enable,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            if (icon != null && iconGravity == IconGravity.START) {
                Icon(
                    modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                    painter = icon,
                    contentDescription = " Icon of $label button",
                    tint = colors.iconColor ?: LocalContentColor.current
                )
            }

            if(!label.isNullOrEmpty()){
                Text(
                    text = label,
                    style = textStyle
                )
            }

            if (icon != null && iconGravity == IconGravity.END) {
                Icon(
                    modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                    painter = icon,
                    contentDescription = " Icon of $label button",
                    tint = colors.iconColor ?: LocalContentColor.current
                )
            }
        }
    }
}