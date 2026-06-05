package com.rdisoftware.chronobeat.presentation.dimensions

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SettingsDimensions(
    val base: BaseDimensions,
    val settingMediumFontSize: TextUnit,
    val settingHeaderFontSize: TextUnit,
    val settingSmallFontSize: TextUnit,
    val toggleWidth: Dp,
    val toggleHeight: Dp,
    val toggleThumbSizeChecked: Dp,
    val toggleThumbSizeUnChecked: Dp,
    val toggleIconSize: Dp,
    val tinySpacer: Dp,
    val smallSpacer: Dp,
    val mediumSpacer: Dp,
    val bottomSpacer: Dp,
    val languageListWidth: Float,
    val supportedLanguageWidth: Float,
    val gradientButtonWidth: Float,
    val languageItemIcon: Dp
)

val PhoneSettingsDimensions = SettingsDimensions(
    base = PhoneBaseDimensions,
    settingMediumFontSize = 32.sp,
    settingHeaderFontSize = 30.sp,
    settingSmallFontSize = 24.sp,
    toggleWidth = 72.dp,
    toggleHeight = 36.dp,
    toggleThumbSizeChecked = 26.dp,
    toggleThumbSizeUnChecked = 23.dp,
    toggleIconSize = 16.dp,
    tinySpacer = 12.dp,
    smallSpacer = 24.dp,
    mediumSpacer = 48.dp,
    bottomSpacer = 32.dp,
    languageListWidth = 0.5f,
    supportedLanguageWidth = 1f,
    gradientButtonWidth = 1f,
    languageItemIcon = 24.dp
)

val TabletSettingsDimensions = SettingsDimensions(
    base = TabletBaseDimensions,
    settingMediumFontSize = 48.sp,
    settingHeaderFontSize = 45.sp,
    settingSmallFontSize = 36.sp,
    toggleWidth = 110.dp,
    toggleHeight = 56.dp,
    toggleThumbSizeChecked = 42.dp,
    toggleThumbSizeUnChecked = 39.dp,
    toggleIconSize = 24.dp,
    tinySpacer = 28.dp,
    smallSpacer = 52.dp,
    mediumSpacer = 84.dp,
    bottomSpacer = 84.dp,
    languageListWidth = 0.4f,
    supportedLanguageWidth = 0.8f,
    gradientButtonWidth = 0.85f,
    languageItemIcon = 34.dp
)

@SuppressLint("CompositionLocalNaming")
val SettingsLocalDimensions = staticCompositionLocalOf { PhoneSettingsDimensions }

val settingsDimens: SettingsDimensions
    @Composable
    get() = SettingsLocalDimensions.current