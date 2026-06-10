package com.rdisoftware.chronobeat.presentation.dimensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BaseDimensions(

    val titleFontSize: TextUnit,
    val logoTextFontSize: TextUnit,
    val bottomTextFontSize: TextUnit,
    val smallButtonFontSize: TextUnit,
    val largeButtonFontSize: TextUnit,
    val maxContentWidth: Dp,
    val contentWidthFraction: Float,
    val maxWidth: Dp
)

val PhoneBaseDimensions = BaseDimensions(
    titleFontSize = 48.sp,
    logoTextFontSize = 24.sp,
    bottomTextFontSize = 16.sp,
    smallButtonFontSize = 24.sp,
    largeButtonFontSize = 32.sp,
    maxContentWidth = 400.dp,
    contentWidthFraction = 0.85f,
    maxWidth = 320.dp
)

val TabletBaseDimensions = BaseDimensions(
    titleFontSize = 80.sp,
    logoTextFontSize = 32.sp,
    bottomTextFontSize = 24.sp,
    smallButtonFontSize = 42.sp,
    largeButtonFontSize = 52.sp,
    maxContentWidth = 700.dp,
    contentWidthFraction = 0.7f,
    maxWidth = 380.dp
)

val LocalBaseDimensions = staticCompositionLocalOf<BaseDimensions> {
    error("No BaseDimensions provided")
}

val baseDimens: BaseDimensions
    @Composable
    get() = LocalBaseDimensions.current
