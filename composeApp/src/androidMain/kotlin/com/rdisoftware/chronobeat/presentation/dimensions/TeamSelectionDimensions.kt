package com.rdisoftware.chronobeat.presentation.dimensions

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TeamSelectionDimensions (
    val base: BaseDimensions,
    val teamInputHeight: Dp,
    val teamRowHeight: Dp,
    val teamRowHorizontalPadding: Dp,
    val teamRowBorderWidth: Dp,
    val roundedCornerShape: Int,
    val teamRowRoundedCornerShape: Int,
    val addIconBoxSize: Dp,
    val inputFontSize: TextUnit,
    val teamNameFontSize: TextUnit,
    val infoMessageFontSize: TextUnit,
    val editDeleteIconsSize: Dp,
    val addIconSize: Dp,
)

val PhoneTeamSelectionDimensions = TeamSelectionDimensions(
    base = PhoneBaseDimensions,
    teamInputHeight = 64.dp,
    teamRowHeight = 40.dp,
    teamRowHorizontalPadding = 16.dp,
    teamRowBorderWidth = 1.5.dp,
    roundedCornerShape = 50,
    addIconBoxSize = 64.dp,
    inputFontSize = 24.sp,
    teamNameFontSize = 22.sp,
    teamRowRoundedCornerShape = 35,
    infoMessageFontSize = 12.sp,
    editDeleteIconsSize = 24.dp,
    addIconSize = 36.dp,
)

val TabletTeamSelectionDimensions = TeamSelectionDimensions(
    base = TabletBaseDimensions,
    teamInputHeight = 100.dp,
    teamRowHeight = 90.dp,
    teamRowHorizontalPadding = 32.dp,
    teamRowBorderWidth = 2.dp,
    roundedCornerShape = 50,
    addIconBoxSize = 96.dp,
    inputFontSize = 38.sp,
    teamNameFontSize = 38.sp,
    teamRowRoundedCornerShape = 30,
    infoMessageFontSize = 20.sp,
    editDeleteIconsSize = 60.dp,
    addIconSize = 54.dp,
)

@SuppressLint("CompositionLocalNaming")
val TeamSelectionLocalDimensions = staticCompositionLocalOf { PhoneTeamSelectionDimensions }

val teamSelDimens: TeamSelectionDimensions
    @Composable
    get() = TeamSelectionLocalDimensions.current
