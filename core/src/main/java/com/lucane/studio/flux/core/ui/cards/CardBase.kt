package com.lucane.studio.flux.core.ui.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lucane.studio.flux.core.theme.AsAHazeStyles
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

@Composable
fun CardBase(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    contentAlignment: Alignment = Alignment.TopStart,
    modularButton: List<@Composable () -> Unit> = emptyList(),
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .border(width = 1.dp, color = Color.White.copy(0.7f), shape = shape)
            .hazeEffect(state = hazeState, style = AsAHazeStyles.cardBlur()),
    ) {
        LazyRow(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(space = 5.dp, alignment = Alignment.End)){
            items(modularButton){ button ->
                button.invoke()
            }
        }
        Box(
            modifier = modifier
                .padding(18.dp),
            contentAlignment = contentAlignment
        ) {
            content.invoke()
        }
    }
}