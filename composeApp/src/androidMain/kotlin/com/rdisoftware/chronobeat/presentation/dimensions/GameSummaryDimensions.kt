package com.rdisoftware.chronobeat.presentation.dimensions

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GameSummaryDimensions(
    val base: BaseDimensions,
    val winnerTextFontSize: TextUnit,
    val spaceByPadding: Dp,
    val columnWidth: Dp
    )

val PhoneGameSumDimensions = GameSummaryDimensions(
    base = PhoneBaseDimensions,
    winnerTextFontSize = 36.sp,
    spaceByPadding = 20.dp,
    columnWidth = 600.dp
)

val TabletGameSumDimensions = GameSummaryDimensions(
    base = TabletBaseDimensions,
    winnerTextFontSize = 48.sp,
    spaceByPadding = 32.dp,
    columnWidth = 700.dp
)

@SuppressLint("CompositionLocalNaming")
val GameSummaryLocalDimensions = staticCompositionLocalOf { PhoneGameSumDimensions }

val gameSumDimens: GameSummaryDimensions
    @Composable
    get() = GameSummaryLocalDimensions.current
