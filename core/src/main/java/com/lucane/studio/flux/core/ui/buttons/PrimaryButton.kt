package com.lucane.studio.flux.core.ui.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.lucane.studio.flux.core.ui.utils.ButtonSize
import com.lucane.studio.flux.core.ui.utils.IconGravity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAButtonColors
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label: String,
    buttonSize: ButtonSize = ButtonSize.S,
    icon: Painter? = null,
    iconGravity: IconGravity = IconGravity.START,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    var modifierResized: Modifier = modifier
    var textStyleResized: TextStyle
    var spacingResized: Dp
    var shapeResized: RoundedCornerShape
    var paddingValuesResized: PaddingValues

    when (buttonSize){
        ButtonSize.XS -> {
            modifierResized = modifier.height(20.dp)
            textStyleResized = TextStyle(
                fontFamily = AsAFont.bold,
                fontSize = 12.sp
            )
            spacingResized = 4.dp
            shapeResized = RoundedCornerShape(6.dp)
            paddingValuesResized = PaddingValues(horizontal = 12.5.dp, vertical = 2.dp)
        }
        else -> {
            modifierResized = modifier.height(32.dp)
            textStyleResized = TextStyle(
                fontFamily = AsAFont.bold,
                fontSize = 14.sp
            )
            spacingResized = 8.dp
            shapeResized = RoundedCornerShape(8.dp)
            paddingValuesResized = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        }
    }

    ButtonBase(
        modifier = modifierResized,
        label = label.lowercase(),
        icon = icon,
        textStyle = textStyleResized,
        iconGravity = iconGravity,
        enable = enable,
        shape = shapeResized,
        spacing = spacingResized,
        paddingValues = paddingValuesResized,
        colors = AsAButtonColors(
            containerColor = AsAColors.purpleNeon,
            labelColor = Color.White,
            iconColor = Color.White
        ),
        onClick = onClick
    )
}