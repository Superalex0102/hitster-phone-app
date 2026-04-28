package com.rdisoftware.chronobeat.ui.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.ui.enums.ButtonSize
import com.rdisoftware.chronobeat.ui.theme.horizontalGradientBrush
import com.rdisoftware.chronobeat.ui.theme.robotoMonoBold

data class ButtonDimensions(
    val widthFraction: Float,
    val ratio: Float,
    val fontSize: TextUnit
)

fun ButtonSize.toDimensions(): ButtonDimensions =
    when (this) {
        ButtonSize.SMALL -> ButtonDimensions(
            widthFraction = 0.5f,
            ratio = 2.9f,
            fontSize = 24.sp
        )

        ButtonSize.LARGE -> ButtonDimensions(
            widthFraction = 0.7f,
            ratio = 3.5f,
            fontSize = 32.sp
        )
    }

@Composable
fun GradientButton(
    text: String,
    enabled: Boolean,
    size: ButtonSize,
    testTag: String,
    resourceId: Boolean,
    onClick: () -> Unit
) {
    val dimensions = size.toDimensions()
    Button(
        modifier = Modifier
            .fillMaxWidth(dimensions.widthFraction)
            .aspectRatio(dimensions.ratio)
            .heightIn(min = 64.dp)
            .alpha(if (enabled) 1f else 0.4f)
            .testTag(testTag)
            .semantics {
                testTagsAsResourceId = resourceId
                role = Role.Button
            }
            .background(
                brush = horizontalGradientBrush,
                shape = ButtonDefaults.shape
            ),
        enabled = enabled,
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        border = BorderStroke(width = 2.dp, color = Color.White)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = dimensions.fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = robotoMonoBold
        )
    }
}