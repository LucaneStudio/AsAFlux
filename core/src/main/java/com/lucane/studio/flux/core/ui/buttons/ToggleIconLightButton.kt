package com.lucane.studio.flux.core.ui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
fun ToggleIconLightButton(
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize = ButtonSize.S,
    icon: Painter,
    checked: Boolean = true,
    onClick: () -> Unit
) {
    var modifierResized: Modifier = modifier
    var spacingResized: Dp
    var iconResized: Dp
    var shapeResized: RoundedCornerShape

    when (buttonSize) {
        ButtonSize.XS -> {
            shapeResized = RoundedCornerShape(6.dp)
            modifierResized = modifier.clip(shapeResized)
                .size(20.dp)
                .background(color = AsAColors.blueNeon.copy(if (checked) 0.8f else 0.05f))
            spacingResized = 4.dp
            iconResized = 14.dp
        }

        else -> {
            shapeResized = RoundedCornerShape(8.dp)
            modifierResized = modifier.clip(shapeResized)
                .size(32.dp)
                .background(color = AsAColors.blueNeon.copy(if (checked) 0.8f else 0.05f))
            spacingResized = 8.dp
            iconResized = 16.dp
        }
    }

    Button(
        modifier = modifierResized,
        shape = shapeResized,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (checked) Color.White else AsAColors.blueNeon,
        ),
        contentPadding = PaddingValues.Zero,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacingResized)
        ) {
            Icon(
                modifier = Modifier.size(iconResized),
                painter = icon,
                contentDescription = " Icon of button",
            )
        }
    }
}