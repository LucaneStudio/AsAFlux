package com.lucane.studio.flux.feature.onboarding.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingModeCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    // TODO DA : remplacer par la vraie card stylisée
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = description)
    }
}