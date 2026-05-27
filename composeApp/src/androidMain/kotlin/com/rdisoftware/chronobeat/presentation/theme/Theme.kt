package com.rdisoftware.chronobeat.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.rdisoftware.chronobeat.R
import com.rdisoftware.chronobeat.theme.AppColors

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
        Color(AppColors.DARK_GRAY),
        Color(AppColors.BLACK)
    )
)
val verticalGradientBrush = Brush.verticalGradient(
    colors = listOf(
        Color(AppColors.BLACK),
        Color(AppColors.DARK_GRAY),
        Color(AppColors.BLACK)
    )
)