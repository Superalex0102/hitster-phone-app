package com.rdisoftware.chronobeat.ui.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.ui.theme.kdamThmorProRegular

@Composable
fun ScreenTitle(
    text: String,
    testTag: String,
    resourceId: Boolean
){
    Text(
        text = text,
        fontSize = 48.sp,
        lineHeight = 70.sp,
        textAlign = TextAlign.Center,
        color = Color.White,
        fontFamily = kdamThmorProRegular,
        modifier = Modifier
            .padding(top = 80.dp, bottom = 24.dp)
            .testTag(testTag)
            .semantics{
                testTagsAsResourceId = resourceId
            }
    )
}