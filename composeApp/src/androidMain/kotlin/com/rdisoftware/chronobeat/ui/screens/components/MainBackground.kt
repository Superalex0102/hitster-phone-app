package com.rdisoftware.chronobeat.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rdisoftware.chronobeat.ui.theme.verticalGradientBrush

@Composable
fun GradientBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = verticalGradientBrush
            )
    )
}