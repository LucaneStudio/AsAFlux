package com.lucane.studio.flux.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.R
import com.lucane.studio.flux.core.utils.LocalApplicationBaseController
import com.lucane.studio.flux.core.ui.utils.EnumPages
import com.lucane.studio.flux.core.utils.LocalHazeController
import dev.chrisbanes.haze.hazeSource

@Composable
fun ApplicationBase(content: @Composable () -> Unit) {
    val hazeState = LocalHazeController.current.mainHazeState
    var selectedPage by remember { mutableStateOf(EnumPages.HOME) }

    val appBase = LocalApplicationBaseController.current
    val header by appBase.header.collectAsState()
    val navbar by appBase.navbar.collectAsState()

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                contentAlignment = Alignment.Center
            ) {
                header?.invoke()
            }

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(59.dp),
                    contentAlignment = Alignment.Center
                ) {
                    navbar?.invoke()
                }
            }
        }
    }
}