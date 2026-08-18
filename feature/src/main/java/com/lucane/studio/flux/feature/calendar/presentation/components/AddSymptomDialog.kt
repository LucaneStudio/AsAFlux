package com.lucane.studio.flux.feature.calendar.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.ui.inputs.buttons.PrimaryButton
import com.lucane.studio.flux.core.ui.model.ButtonSize
import com.lucane.studio.flux.core.ui.textfield.PrimaryTextField
import com.lucane.studio.flux.core.R as CoreRes

/**
 * Modal to name a new custom symptom before it joins the catalog.
 * No final design yet — styled from the existing palette/components,
 * pending a mockup pass.
 */
// TODO DA: layout placeholder — pending the final visual design pass
@Composable
fun AddSymptomDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    val shape = RoundedCornerShape(16.dp)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(Color.White, shape)
                .border(1.dp, AsAColors.purpleNeon.copy(alpha = 0.2f), shape)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(CoreRes.string.tracking_add_symptom_title),
                style = TextStyle(fontFamily = AsAFont.semiBold, fontSize = 16.sp, color = AsAColors.black),
            )

            PrimaryTextField(
                modifier = Modifier.fillMaxWidth().height(44.dp),
                value = name,
                onValueChange = { name = it },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onDismiss)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    text = stringResource(CoreRes.string.common_cancel),
                    style = TextStyle(fontFamily = AsAFont.medium, fontSize = 14.sp, color = AsAColors.purpleGray),
                )

                PrimaryButton(
                    label = stringResource(CoreRes.string.common_confirm),
                    buttonSize = ButtonSize.S,
                    enable = name.isNotBlank(),
                    onClick = { onConfirm(name) },
                )
            }
        }
    }
}
