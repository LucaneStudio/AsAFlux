package com.lucane.studio.flux.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.ui.buttons.IconLightBlurButton
import com.lucane.studio.flux.core.ui.buttons.LightBlurButton
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.R
import com.lucane.studio.flux.core.R as CoreRes

@Composable
fun HomeHeaderRow(
    modifier: Modifier = Modifier.fillMaxWidth(),
    hazeState: HazeState
){
    Row(
        modifier.padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            LightBlurButton(
                icon = painterResource(CoreRes.drawable.ic_tray_in),
                label = stringResource(CoreRes.string.tray_in),
                hazeState = hazeState
            ) { }

            LightBlurButton(
                icon = painterResource(CoreRes.drawable.ic_tray_out),
                label = stringResource(CoreRes.string.tray_out),
                hazeState = hazeState
            ) { }
        }

        IconLightBlurButton(
            icon = painterResource(CoreRes.drawable.ic_more),
            hazeState = hazeState,
        ) { }
    }
}