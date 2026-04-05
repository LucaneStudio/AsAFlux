package com.lucane.studio.flux.core.ui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.clip
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
import com.lucane.studio.flux.core.theme.AsAHazeStyles
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.FluentMaterials
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun LightBlurButton(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
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
    var iconResized: Dp
    var shapeResized: RoundedCornerShape
    var paddingValuesResized: PaddingValues

    when (buttonSize) {
        ButtonSize.XS -> {
            shapeResized = RoundedCornerShape(6.dp)
            modifierResized = modifier.clip(shapeResized)
                .hazeEffect(state = hazeState, style = AsAHazeStyles.lightBlur())
                .height(20.dp)
            textStyleResized = TextStyle(
                fontFamily = AsAFont.bold,
                fontSize = 12.sp,
                color = AsAColors.blueNeon
            )
            spacingResized = 4.dp
            paddingValuesResized = PaddingValues(horizontal = 12.5.dp, vertical = 2.dp)
            iconResized = 14.dp
        }

        else -> {
            shapeResized = RoundedCornerShape(8.dp)
            modifierResized = modifier.clip(shapeResized)
                .hazeEffect(state = hazeState, style = AsAHazeStyles.lightBlur())
                .height(32.dp)
            textStyleResized = TextStyle(
                fontFamily = AsAFont.bold,
                fontSize = 14.sp,
                color = AsAColors.blueNeon
            )
            spacingResized = 8.dp
            paddingValuesResized = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            iconResized = 16.dp
        }
    }

    Button(
        modifier = modifierResized,
        shape = shapeResized,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = AsAColors.blueNeon,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.White.copy(0.7f)
        ),
        contentPadding = paddingValuesResized,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacingResized)
        ) {
            if (icon != null && iconGravity == IconGravity.START) {
                Image(
                    modifier = Modifier.size(iconResized),
                    painter = icon,
                    contentDescription = " Icon of $label button",
                )
            }

            if(!label.isNullOrEmpty()){
                Text(
                    text = label,
                    style = textStyleResized
                )
            }

            if (icon != null && iconGravity == IconGravity.END) {
                Image(
                    modifier = Modifier.size(iconResized),
                    painter = icon,
                    contentDescription = " Icon of $label button",
                )
            }
        }
    }
}