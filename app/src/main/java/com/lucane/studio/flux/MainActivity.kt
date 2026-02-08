package com.lucane.studio.flux

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.ui.buttons.PrimaryButton
import com.lucane.studio.flux.core.ui.utils.ButtonSize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        // Déplace requestedOrientation ICI, avant setContent
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(){
    Column(Modifier.padding(start = 80.dp)) {
        PrimaryButton(
            label = "Début des règles"
        ) { }

        PrimaryButton(
            label = "Valider",
            buttonSize = ButtonSize.XS
        ) { }
    }
}
