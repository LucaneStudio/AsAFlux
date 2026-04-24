package com.lucane.studio.flux.core.ui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont

@Composable
fun PrimaryTextFieldButton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    value: String,
    endItem: (@Composable () -> Unit)? = null,
    startItem: (@Composable () -> Unit)? = null,
    borderColor: Color = AsAColors.purpleNeon.copy(0.2f),
    onClick: () -> Unit
){
    Button(
        modifier = modifier,
        colors = ButtonColors(
            containerColor = Color.White.copy(0.3f),
            contentColor = AsAColors.purpleNeon,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor,
        ),
        shape = shape,
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            startItem?.invoke()
            Text(
                modifier = Modifier.weight(1f),
                text = value,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontFamily = AsAFont.regular,
                    fontSize = 14.sp,
                    color = AsAColors.black,
                )
            )
            endItem?.invoke()
        }
    }
}