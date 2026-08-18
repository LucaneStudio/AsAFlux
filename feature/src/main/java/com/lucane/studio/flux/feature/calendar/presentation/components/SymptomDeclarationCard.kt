package com.lucane.studio.flux.feature.calendar.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.ui.inputs.buttons.IconLightButton
import com.lucane.studio.flux.core.ui.inputs.buttons.PrimaryButton
import com.lucane.studio.flux.core.ui.cards.CardBase
import com.lucane.studio.flux.core.ui.model.ButtonSize
import com.lucane.studio.flux.core.ui.textfield.PrimaryTextField
import com.lucane.studio.flux.core.providers.LocalHazeController
import com.lucane.studio.flux.data.model.FlowIntensity
import com.lucane.studio.flux.data.model.Symptom
import com.lucane.studio.flux.data.model.SymptomCategory
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.lucane.studio.flux.core.R
import com.lucane.studio.flux.core.ui.inputs.toggles.CheckmarkToggle
import com.lucane.studio.flux.core.ui.inputs.toggles.RadioLine
import com.lucane.studio.flux.core.ui.inputs.toggles.RadioLineItemData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.lucane.studio.flux.core.R as CoreRes

private val SectionLabelStyle = TextStyle(
    fontFamily = AsAFont.semiBold,
    fontSize = 13.sp,
    color = AsAColors.black,
)

/**
 * Full day-declaration panel for the tracking screen: flow intensity, physical
 * and psychological symptoms, pain intensity, and a free-text note — all for
 * whichever day is currently selected on the calendar above it.
 */
// TODO DA: layout placeholder — pending the final visual design pass
@Composable
fun SymptomDeclarationCard(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    flowIntensity: FlowIntensity,
    painLevel: Int?,
    allSymptoms: List<Symptom>,
    selectedSymptoms: List<Symptom>,
    note: String,
    canValidate: Boolean = true,
    onFlowIntensityChange: (FlowIntensity) -> Unit,
    onPainLevelChange: (Int) -> Unit,
    onSymptomToggle: (Symptom) -> Unit,
    onAddCustomSymptom: (name: String, category: SymptomCategory) -> Unit,
    onNoteChange: (String) -> Unit,
    onValidate: () -> Unit,
    onReset: () -> Unit,
) {
    val hazeState = LocalHazeController.current.mainHazeState
    val dateLabel = selectedDate.format(DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.getDefault()))
    val physicalSymptoms = allSymptoms.filter { it.category == SymptomCategory.PHYSICAL }
    val psychologicalSymptoms = allSymptoms.filter { it.category == SymptomCategory.EMOTIONAL }

    // Which section the "+ ajouter" dialog is being filled out for, if any.
    var addSymptomCategory by remember { mutableStateOf<SymptomCategory?>(null) }

    CardBase(modifier = modifier.fillMaxWidth(), hazeState = hazeState) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = stringResource(CoreRes.string.tracking_declare_symptoms_title),
                        style = TextStyle(fontFamily = AsAFont.semiBold, fontSize = 16.sp, color = AsAColors.black),
                    )
                    Text(
                        text = dateLabel.replaceFirstChar { it.titlecase(Locale.getDefault()) },
                        style = TextStyle(fontFamily = AsAFont.regular, fontSize = 12.sp, color = AsAColors.purpleGray),
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (canValidate) {
                        IconLightButton(
                            icon = painterResource(CoreRes.drawable.ic_cancel),
                            buttonSize = ButtonSize.S,
                            onClick = onReset,
                        )
                    }
                    PrimaryButton(
                        label = stringResource(CoreRes.string.tracking_validate),
                        buttonSize = ButtonSize.S,
                        enable = canValidate,
                        onClick = onValidate,
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = stringResource(CoreRes.string.tracking_section_flow_intensity), style = SectionLabelStyle)

                RadioLine(
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    items = listOf(
                        RadioLineItemData(
                            id = FlowIntensity.NONE.name,
                            labelRes = CoreRes.string.tracking_flow_none,
                            iconRes = CoreRes.drawable.ic_cancel,
                            isSelected = flowIntensity == FlowIntensity.NONE
                        ),
                        RadioLineItemData(
                            id = FlowIntensity.LIGHT.name,
                            labelRes = CoreRes.string.tracking_flow_light,
                            iconRes = CoreRes.drawable.ic_signal_bad,
                            isSelected = flowIntensity == FlowIntensity.LIGHT
                        ),
                        RadioLineItemData(
                            id = FlowIntensity.MEDIUM.name,
                            labelRes = CoreRes.string.tracking_flow_medium,
                            iconRes = CoreRes.drawable.ic_signal_low,
                            isSelected = flowIntensity == FlowIntensity.MEDIUM
                        ),
                        RadioLineItemData(
                            id = FlowIntensity.HEAVY.name,
                            labelRes = CoreRes.string.tracking_flow_heavy,
                            iconRes = CoreRes.drawable.ic_signal_medium,
                            isSelected = flowIntensity == FlowIntensity.HEAVY
                        )
                    )
                ) { id ->
                    onFlowIntensityChange(
                        when (id) {
                            FlowIntensity.LIGHT.name -> FlowIntensity.LIGHT
                            FlowIntensity.MEDIUM.name -> FlowIntensity.MEDIUM
                            FlowIntensity.HEAVY.name -> FlowIntensity.HEAVY
                            FlowIntensity.NONE.name -> FlowIntensity.NONE
                            else -> FlowIntensity.NOT_DECLARED
                        }
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = stringResource(CoreRes.string.tracking_section_physical_symptoms), style = SectionLabelStyle)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    physicalSymptoms.forEach { symptom ->
                        CheckmarkToggle(
                            label = symptom.name.lowercase(),
                            isChecked = selectedSymptoms.any { it.id == symptom.id },
                            onCheckedChange = {
                                onSymptomToggle(symptom)
                            }
                        )
                    }
                    AddSymptomChip(onClick = { addSymptomCategory = SymptomCategory.PHYSICAL })
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = stringResource(CoreRes.string.tracking_section_psychological_symptoms), style = SectionLabelStyle)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {
                    psychologicalSymptoms.forEach { symptom ->
                        CheckmarkToggle(
                            label = symptom.name.lowercase(),
                            isChecked = selectedSymptoms.any { it.id == symptom.id },
                            onCheckedChange = {
                                onSymptomToggle(symptom)
                            }
                        )
                    }
                    AddSymptomChip(onClick = { addSymptomCategory = SymptomCategory.EMOTIONAL })
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = stringResource(CoreRes.string.tracking_section_pain_intensity), style = SectionLabelStyle)

                RadioLine(
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    items = listOf(
                        RadioLineItemData(
                            id = 0.toString(),
                            labelRes = CoreRes.string.tracking_pain_none,
                            iconRes = CoreRes.drawable.ic_cancel,
                            isSelected = painLevel == 0 || painLevel == null
                        ),
                        RadioLineItemData(
                            id = 1.toString(),
                            labelRes = CoreRes.string.tracking_pain_light,
                            iconRes = CoreRes.drawable.ic_signal_bad,
                            isSelected = painLevel == 1
                        ),
                        RadioLineItemData(
                            id = 2.toString(),
                            labelRes = CoreRes.string.tracking_pain_medium,
                            iconRes = CoreRes.drawable.ic_signal_low,
                            isSelected = painLevel == 2
                        ),
                        RadioLineItemData(
                            id = 3.toString(),
                            labelRes = CoreRes.string.tracking_pain_strong,
                            iconRes = CoreRes.drawable.ic_signal_medium,
                            isSelected = painLevel == 3
                        ),
                        RadioLineItemData(
                            id = 4.toString(),
                            labelRes = CoreRes.string.tracking_pain_very_strong,
                            iconRes = CoreRes.drawable.ic_signal_full,
                            isSelected = painLevel == 4
                        )
                    )
                ) { id ->
                    onPainLevelChange(
                        id.toInt()
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = stringResource(CoreRes.string.tracking_note_label), style = SectionLabelStyle)
                PrimaryTextField(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    value = note,
                    onValueChange = onNoteChange,
                )
            }
        }
    }

    addSymptomCategory?.let { category ->
        AddSymptomDialog(
            onDismiss = { addSymptomCategory = null },
            onConfirm = { name ->
                onAddCustomSymptom(name, category)
                addSymptomCategory = null
            },
        )
    }
}

/** The trailing "+ ajouter" pill at the end of a symptom section, opening [AddSymptomDialog]. */
@Composable
private fun AddSymptomChip(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier = modifier
            .clip(shape)
            .background(Color.White.copy(alpha = 0.5f))
            .clickable {onClick.invoke()}
            .padding(horizontal = 7.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            painter = painterResource(CoreRes.drawable.ic_plus),
            contentDescription = "CardHeader Icon",
            tint = AsAColors.purpleLightGray
        )

        Text(
            text = stringResource(CoreRes.string.tracking_add_symptom),
            style = TextStyle(
                fontFamily = AsAFont.bold,
                fontSize = 13.sp,
                color = AsAColors.purpleLightGray,
            ),
        )
    }
}
