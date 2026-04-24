package com.lucane.studio.flux.core.ui.textfield

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont

@Composable
fun PrimaryTextField(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorLabel: String? = null,
    endItem: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit,
){
    Column {
        Row(
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = if (isError) AsAColors.redNeon else AsAColors.purpleNeon.copy(0.2f),
                    shape = shape
                )
                .background(
                    color = Color.White.copy(0.3f),
                    shape = shape
                )
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextFieldBase(
                modifier = Modifier.weight(1f),
                value = value,
                textStyle = TextStyle(
                    fontFamily = AsAFont.regular,
                    fontSize = 14.sp,
                    color = AsAColors.black,
                ),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                onValueChange = onValueChange
            )
            endItem?.invoke()
        }
        if(!errorLabel.isNullOrEmpty() && isError) {
            Spacer(Modifier.size(3.dp))
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = errorLabel,
                style = TextStyle(
                    fontFamily = AsAFont.regular,
                    fontSize = 10.sp,
                    color = AsAColors.redNeon,
                )
            )
        }
    }

}