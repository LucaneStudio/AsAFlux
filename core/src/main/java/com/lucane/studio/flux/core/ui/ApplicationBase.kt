package com.lucane.studio.flux.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.R
import com.lucane.studio.flux.core.ui.utils.EnumPages
import com.lucane.studio.flux.core.utils.LocalHazeController
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun ApplicationBase(content: @Composable () -> Unit) {
    val hazeState = LocalHazeController.current.mainHazeState
    var selectedPage by remember { mutableStateOf(EnumPages.HOME) }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState),
            painter = painterResource(R.drawable.background),
            contentScale = ContentScale.FillHeight,
            contentDescription = "Application Background"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 15.dp)
        ) {
            HomeHeaderRow(hazeState = hazeState)
            Column(
                modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content.invoke()
                }
                Spacer(Modifier.size(8.dp))
                Navbar(
                    hazeState = hazeState,
                    selectedPageId = selectedPage
                ) {
                    selectedPage = it
                }
            }

        }
    }
}