package com.rdisoftware.chronobeat.presentation.dimensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class HomeDimensions(
    val base: BaseDimensions,
    val popupTitleFontSize: TextUnit,
    val errorTextFontSize: TextUnit,
    val popupTitleLineHeight: TextUnit,
    val basePadding: Dp,
    val screenEdgePadding: Dp,
    val popupMaxContentWidth: Dp,
    val settingsIconSize: Dp,
    val spacerTopWeight: Float,
    val spacerMiddleWeight: Float,
    val spacerBottomWeight: Float,
)

val PhoneHomeDimensions = HomeDimensions(
    base = PhoneBaseDimensions,
    popupTitleFontSize = 28.sp,
    errorTextFontSize = 14.sp,
    popupTitleLineHeight = 40.sp,
    basePadding = 24.dp,
    screenEdgePadding = 16.dp,
    settingsIconSize = 34.dp,
    popupMaxContentWidth = 400.dp,
    spacerTopWeight = 1.5f,
    spacerMiddleWeight = 2f,
    spacerBottomWeight = 4f,
)

val TabletHomeDimensions = HomeDimensions(
    base = TabletBaseDimensions,
    popupTitleFontSize = 50.sp,
    errorTextFontSize = 22.sp,
    popupTitleLineHeight = 46.sp,
    basePadding = 40.dp,
    screenEdgePadding = 32.dp,
    settingsIconSize = 100.dp,
    popupMaxContentWidth = 600.dp,
    spacerTopWeight = 2f,
    spacerMiddleWeight = 3f,
    spacerBottomWeight = 5f
)

val HomeLocalDimensions = staticCompositionLocalOf { PhoneHomeDimensions }

val homeDimens: HomeDimensions
    @Composable
    get() = HomeLocalDimensions.current