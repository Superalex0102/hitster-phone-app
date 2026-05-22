package com.rdisoftware.chronobeat.presentation.dimensions

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class GameSummaryDimensions(
    val base: BaseDimensions,
    val winnerTextFontSize: TextUnit
)

val PhoneGameSumDimensions = GameSummaryDimensions(
    base = PhoneBaseDimensions,
    winnerTextFontSize = 36.sp
)

val TabletGameSumDimensions = GameSummaryDimensions(
    base = TabletBaseDimensions,
    winnerTextFontSize = 48.sp
)

@SuppressLint("CompositionLocalNaming")
val GameSummaryLocalDimensions = staticCompositionLocalOf { PhoneGameSumDimensions }

val gameSumDimens: GameSummaryDimensions
    @Composable
    get() = GameSummaryLocalDimensions.current
