package com.rdisoftware.chronobeat.presentation.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.Common.BOTTOM_TEXT
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoBold
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoLight
import com.rdisoftware.chronobeat.theme.AppColors

@Composable
fun BottomText(
    text: String,
    name: String
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(AppColors.BOTTOM_GRAY), fontFamily = robotoMonoLight)) {
                append(text)
            }
            withStyle(style = SpanStyle(color = Color(AppColors.BOTTOM_GRAY), fontFamily = robotoMonoBold)) {
                append(name)
            }
        },
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .testTag(BOTTOM_TEXT)
            .semantics {
                testTagsAsResourceId = true
            },
        textAlign = TextAlign.Center
    )
}