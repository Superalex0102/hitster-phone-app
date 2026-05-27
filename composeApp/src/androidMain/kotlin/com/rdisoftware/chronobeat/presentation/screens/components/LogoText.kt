package com.rdisoftware.chronobeat.presentation.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.Common.LOGO_TEXT
import com.rdisoftware.chronobeat.presentation.dimensions.baseDimens
import com.rdisoftware.chronobeat.presentation.theme.kdamThmorProRegular
import com.rdisoftware.chronobeat.shared.resources.*
import com.rdisoftware.chronobeat.theme.AppColors
import org.jetbrains.compose.resources.stringResource

@Composable
fun LogoText() {

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp),
        ) {
            Text(
                text = stringResource(Res.string.title),
                fontSize = baseDimens.logoTextFontSize,
                color = Color(AppColors.WHITE),
                fontFamily = kdamThmorProRegular,
                modifier = Modifier
                    .testTag(LOGO_TEXT)
                    .underline()
                    .semantics {
                        testTagsAsResourceId = true
                    }
            )

            Spacer(modifier = Modifier.weight(0.95f))
        }
    }
}
fun Modifier.underline(
    color: Color = Color(AppColors.WHITE),
    thickness: Dp = 2.dp,
    paddingValues: Dp = 4.dp,
): Modifier =  this.drawBehind{
    val strokeWidth = thickness.toPx()
    val paddingPx = paddingValues.toPx()
    val y = size.height - strokeWidth / 2 + paddingPx
    drawLine(
        color = color,
        start = Offset(0f, y),
        end = Offset(size.width, y),
        strokeWidth = strokeWidth
    )
}