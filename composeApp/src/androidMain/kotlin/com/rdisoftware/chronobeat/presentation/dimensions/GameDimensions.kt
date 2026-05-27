package com.rdisoftware.chronobeat.presentation.dimensions

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GameDimensions(
    val base: BaseDimensions,
    val currentTeamNameFontSize: TextUnit,
    val cardCountNumberFontSize: TextUnit,
    val cardCountBoxSize: Dp,
    val guessButtonOuterCircle: Dp,
    val guessButtonInnerCircle: Dp,
    val mainArtistFontSize: TextUnit,
    val featArtistFontSize: TextUnit,
    val releaseYearFontSize: TextUnit,
    val trackTitleFontSize: TextUnit,
    val arrowWidthPosition: Dp,
    val cardMaxWidthFraction: Float,
    val arrowTextOffset: Dp
)

val PhoneGameDimensions = GameDimensions(
    base = PhoneBaseDimensions,
    currentTeamNameFontSize = 32.sp,
    cardCountNumberFontSize = 24.sp,
    cardCountBoxSize = 52.dp,
    guessButtonOuterCircle = 64.dp,
    guessButtonInnerCircle = 32.dp,
    mainArtistFontSize = 24.sp,
    featArtistFontSize = 12.sp,
    releaseYearFontSize = 40.sp,
    trackTitleFontSize = 18.sp,
    arrowWidthPosition = 80.dp,
    cardMaxWidthFraction = 1f,
    arrowTextOffset = 32.dp
)

val TabletGameDimensions = GameDimensions(
    base = TabletBaseDimensions,
    currentTeamNameFontSize = 52.sp,
    cardCountNumberFontSize = 40.sp,
    cardCountBoxSize = 70.dp,
    guessButtonOuterCircle = 90.dp,
    guessButtonInnerCircle = 45.dp,
    mainArtistFontSize = 46.sp,
    featArtistFontSize = 26.sp,
    releaseYearFontSize = 78.sp,
    trackTitleFontSize = 30.sp,
    arrowWidthPosition = 200.dp,
    cardMaxWidthFraction = 0.7f,
    arrowTextOffset = 90.dp
)

@SuppressLint("CompositionLocalNaming")
val GameLocalDimensions = staticCompositionLocalOf { PhoneGameDimensions }

val gameDimens: GameDimensions
    @Composable
    get() = GameLocalDimensions.current