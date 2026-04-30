package com.rdisoftware.chronobeat.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.rdisoftware.chronobeat.R

val kdamThmorProRegular = FontFamily(
    Font(R.font.kdam_thmor_pro_regular)
)
val robotoMonoRegular = FontFamily(
    Font(R.font.roboto_mono_regular)
)
val robotoMonoBold = FontFamily(
    Font(R.font.roboto_mono_bold)
)
val robotoMonoMedium = FontFamily(
    Font(R.font.roboto_mono_medium)
)
val robotoMonoLightItalic = FontFamily(
    Font(R.font.roboto_mono_light_italic)
)
val robotoMonoLight = FontFamily(
    Font(R.font.roboto_mono_light)
)

val horizontalGradientBrush = Brush.horizontalGradient(
    colors = listOf(
    Color.Gray,
    Color.DarkGray,
    Color.Black
))
val verticalGradientBrush = Brush.verticalGradient(
    colors = listOf(
        Color.Black,
        Color.DarkGray,
        Color.Black
    ))