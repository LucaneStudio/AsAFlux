package com.lucane.studio.flux

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lucane.studio.flux.core.utils.HazeController
import com.lucane.studio.flux.core.utils.LocalHazeController
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.ui.ApplicationBase
import com.lucane.studio.flux.core.ui.cards.CardWithHeader
import com.lucane.studio.flux.core.ui.utils.HeaderInfos
import com.lucane.studio.flux.feature.calendar.presentation.screen.calendar.CalendarScreen
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.haze.rememberHazeState
import com.lucane.studio.flux.core.R as CoreRes

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            val systemUiController = rememberSystemUiController()
            val hazeState = rememberHazeState()

            WindowCompat.setDecorFitsSystemWindows(window, false)

            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = true
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                navigationBarContrastEnforced = false,
                darkIcons = true
            )
            CompositionLocalProvider(
                LocalHazeController provides HazeController(hazeState)
            ) {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){
    ApplicationBase {
        CalendarScreen()

        CardWithHeader(
            modifier = Modifier.fillMaxWidth(),
            headerInfos = HeaderInfos(
                labelRes = stringResource(CoreRes.string.daily_sensation),
                iconRes = CoreRes.drawable.ic_chevron_end,
                onClick = {}
            )
        ) { }

        CardWithHeader(
            modifier = Modifier.fillMaxWidth(),
            headerInfos = HeaderInfos(
                labelRes = stringResource(CoreRes.string.my_cycles),
                iconRes = CoreRes.drawable.ic_chevron_end,
                subLabelRes = stringResource(CoreRes.string.cycles_entered, 135),
                onClick = {}
            )
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(96.dp)
                        .background(
                            AsAColors.blueNeon.copy(0.2f),
                            RoundedCornerShape(6.dp)
                        )
                )

                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(96.dp)
                        .background(
                            AsAColors.blueNeon.copy(0.2f),
                            RoundedCornerShape(6.dp)
                        )
                )
            }
        }
    }
}