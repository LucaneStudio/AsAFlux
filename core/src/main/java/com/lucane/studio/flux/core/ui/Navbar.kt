package com.lucane.studio.flux.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucane.studio.flux.core.R
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.theme.AsAHazeStyles
import com.lucane.studio.flux.core.ui.utils.EnumPages
import com.lucane.studio.flux.core.ui.utils.IconGravity
import com.lucane.studio.flux.core.utils.LocalHazeController
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import com.lucane.studio.flux.core.R as CoreRes

@Composable
fun Navbar(
    selectedPageId: EnumPages,
    onClick: (EnumPages) -> Unit
){
    val shape = RoundedCornerShape(12.dp)
    val hazeState = LocalHazeController.current.mainHazeState

    Box(
        modifier = Modifier.fillMaxWidth().height(59.dp)
            .clip(shape)
            .border(width = 1.dp, color = Color.White.copy(0.7f), shape = shape)
            .hazeEffect(state = hazeState, style = AsAHazeStyles.cardBlur()),
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            NavbarBUtton(
                modifier = Modifier.fillMaxHeight().weight(1f),
                defaultIcon = painterResource(CoreRes.drawable.ic_home_outline),
                selectedIcon = painterResource(CoreRes.drawable.ic_home_solid),
                label = stringResource(CoreRes.string.nav_home),
                pageId = EnumPages.HOME,
                isSelected = selectedPageId == EnumPages.HOME,
                onClick = onClick
            )

            NavbarBUtton(
                modifier = Modifier.fillMaxHeight().weight(1f),
                defaultIcon = painterResource(CoreRes.drawable.ic_calendar_outline),
                selectedIcon = painterResource(CoreRes.drawable.ic_calendar_solid),
                label = stringResource(CoreRes.string.nav_calendar),
                pageId = EnumPages.CALENDAR,
                isSelected = selectedPageId == EnumPages.CALENDAR,
                onClick = onClick
            )

            NavbarBUtton(
                modifier = Modifier.fillMaxHeight().weight(1f),
                defaultIcon = painterResource(CoreRes.drawable.ic_detail_outline),
                selectedIcon = painterResource(CoreRes.drawable.ic_details_solid),
                label = stringResource(CoreRes.string.nav_details),
                pageId = EnumPages.DETAILS,
                isSelected = selectedPageId == EnumPages.DETAILS,
                onClick = onClick
            )

            NavbarBUtton(
                modifier = Modifier.fillMaxHeight().weight(1f),
                defaultIcon = painterResource(CoreRes.drawable.ic_notes_outline),
                selectedIcon = painterResource(CoreRes.drawable.ic_notes_solid),
                label = stringResource(CoreRes.string.nav_notes),
                pageId = EnumPages.NOTES,
                isSelected = selectedPageId == EnumPages.NOTES,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun NavbarBUtton(
    modifier: Modifier,
    defaultIcon: Painter,
    selectedIcon: Painter,
    label: String,
    pageId: EnumPages,
    isSelected: Boolean,
    onClick: (EnumPages) -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            contentColor = AsAColors.blueNeon,
            containerColor = Color.Transparent,
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        enabled = !isSelected,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues.Zero,
        onClick = { onClick.invoke(pageId) }
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Icon(
                modifier = Modifier.size(24.dp),
                painter = if (isSelected) selectedIcon else defaultIcon,
                contentDescription = " Icon of $label button",
                tint = if (isSelected) AsAColors.blueNeon else AsAColors.purpleGray
            )

            Text(
                text = label,
                style = TextStyle(
                    fontFamily = if (isSelected) AsAFont.bold else AsAFont.medium,
                    fontSize = 12.sp,
                    color = if (isSelected) AsAColors.blueNeon else AsAColors.purpleGray,
                ),
            )

        }
    }
}