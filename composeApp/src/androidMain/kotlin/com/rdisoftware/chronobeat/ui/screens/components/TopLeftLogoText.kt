package com.rdisoftware.chronobeat.ui.screens.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.shared.resources.*
import org.jetbrains.compose.resources.stringResource
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.Common.TOP_LEFT_LOGO_TEXT
import com.rdisoftware.chronobeat.ui.theme.kdamThmorProRegular

@Composable
fun TopLeftLogoText(){
    Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp)) {
        Text(text = stringResource(Res.string.title),
            fontSize = 24.sp,
            color = Color.White,
            fontFamily = kdamThmorProRegular,
            modifier = Modifier.testTag(TOP_LEFT_LOGO_TEXT))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(0.44f),
            thickness = 2.dp,
            color = Color.Gray,

        )
        Spacer(modifier = Modifier.weight(0.95f))
    }

}